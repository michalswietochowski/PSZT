package pl.edu.pw.elka.pszt.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import pl.edu.pw.elka.pszt.game.AStar;
import pl.edu.pw.elka.pszt.game.Move;
import pl.edu.pw.elka.pszt.models.*;
import pl.edu.pw.elka.pszt.utils.LevelFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * PSZT
 * Created: 24.11.2013 16:45
 */
public class GameStatusController implements Observer {

    private static final int LEVELS_NUM = 3;
    private static final int TILE_SIZE = 64;
    private static DateFormat DATEFORMAT =  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SS");
    private final ObservableList<AStar> threadList = FXCollections.observableArrayList();
    public ComboBox levelComboBox;
    public GridPane levelGridPane;
    public Button solveButton;
    public TableView statsTableView;
    public ListView statusListView;
    public ProgressIndicator progressIndicator;
    public Label stepsLabel;
    public Button stepsPrevButton, stepsNextButton;
    public Button stopButton;
    public TextField loopsRemovalCountFromTextField;
    public TextField loopsRemovalCountToTextField;
    public ToggleGroup algorithm;
    private int currentStep;
    private String currentThread;
    private ArrayList<Move> currentMoves;
    private int currentMovesSize;

    public void initialize() {
        for (int i = 1; i <= LEVELS_NUM; i++) {
            levelComboBox.getItems().add("Level " + i);
        }
        TableColumn nameColumn = (TableColumn) statsTableView.getColumns().get(0);
        TableColumn valueColumn = (TableColumn) statsTableView.getColumns().get(1);
        nameColumn.setCellValueFactory(new PropertyValueFactory<StatsParam, String>("name"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<StatsParam, String>("value"));
        algorithm.getToggles().get(0).setUserData("astar");
        algorithm.getToggles().get(1).setUserData("idastar");
    }

    public void onLevelChange(ActionEvent actionEvent) {
        levelGridPane.getChildren().clear();
        try {
            drawLevel(LevelFactory.createFromProperties(getCurrentLevelName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        resetGUIState();
        statusListView.setDisable(true);
        stepsLabel.setVisible(false);
        stepsPrevButton.setVisible(false);
        stepsPrevButton.setDisable(true);
        stepsNextButton.setVisible(false);
        stepsNextButton.setDisable(false);
        statusListView.getItems().clear();
        statsTableView.getItems().clear();
        threadList.clear();

        int selectedLevel = getSelectedLevelNum();
        switch (selectedLevel) {
            case 1:
                loopsRemovalCountFromTextField.setText("4");
                loopsRemovalCountToTextField.setText("7");
                break;
            case 2:
                loopsRemovalCountFromTextField.setText("7");
                loopsRemovalCountToTextField.setText("10");
                break;
            case 3:
                loopsRemovalCountFromTextField.setText("4");
                loopsRemovalCountToTextField.setText("12");
                break;
        }
    }

    protected int getSelectedLevelNum() {
        return levelComboBox.getSelectionModel().getSelectedIndex() + 1;
    }

    protected String getCurrentLevelName() {
        return "level" + getSelectedLevelNum();
    }

    protected void drawLevel(Level level) {
        Map levelMap = level.getMap();
        MovablesMap levelMovables = level.getMovablesMap();

        for (int x = 0; x < levelMap.getWidth(); x++) {
            for (int y = 0; y < levelMap.getHeight(); y++) {
                Canvas canvas = new Canvas(TILE_SIZE, TILE_SIZE);
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.drawImage(levelMap.getMapObject(x, y).getImage(TILE_SIZE, TILE_SIZE), 0, 0);

                if (levelMovables.getMovableObject(x, y) != null) {
                    //todo: bulldozer rotation based on last move?
                    gc.drawImage(levelMovables.getMovableObject(x, y).getImage(TILE_SIZE, TILE_SIZE), 0, 0);
                }

                levelGridPane.add(canvas, x, y);
            }
        }
    }

    public void onSolveClick(ActionEvent actionEvent) {
        if (validateInputs()) {
            resetGUIState();
            solveButton.setDisable(true);
            progressIndicator.setVisible(true);
            stopButton.setVisible(true);
            statusListView.setDisable(false);
            stepsLabel.setVisible(false);
            stepsPrevButton.setVisible(false);
            stepsPrevButton.setDisable(true);
            stepsNextButton.setVisible(false);
            stepsNextButton.setDisable(false);
            statusListView.getItems().clear();
            statsTableView.getItems().clear();
            threadList.clear();
            startSolving(levelComboBox.getSelectionModel().getSelectedIndex() + 1);
        }
    }

    protected boolean validateInputs() {
        String lrf = loopsRemovalCountFromTextField.getText(), lrt = loopsRemovalCountToTextField.getText();
        if (lrf.isEmpty() || lrt.isEmpty()) {
            Dialogs.showWarningDialog(null, "Remove loops text fields cannot be empty.");
            return false;
        }
        try {
            int removeLoopsFrom = Integer.parseInt(lrf);
            int removeLoopsTo = Integer.parseInt(lrt);

            if (removeLoopsFrom <= 0 || removeLoopsTo <= 0) {
                Dialogs.showWarningDialog(null, "Remove loops text fields must contain positive numbers.");
                return false;
            }
            if (removeLoopsFrom > removeLoopsTo) {
                Dialogs.showWarningDialog(null, "Remove loops \"to\" must be higher than or equal \"from\" value.");
                return false;
            }
        } catch (NumberFormatException e) {
            Dialogs.showWarningDialog(null, "Remove loops text fields cannot be parsed as numbers.");
            return false;
        } catch (Exception e) {
            Dialogs.showWarningDialog(null, "Remove loops text fields invalid input.");
            return false;
        }
        return true;
    }

    protected void startSolving(int levelNum) {
        solve(levelNum);
    }

    protected ArrayList<BarrelSpotPair<Barrel, Spot>> getBarrelSpotPair(Level level) {
        ArrayList<Barrel> barrels = level.getMovablesMap().getBarrels();
        ArrayList<Spot> spots = level.getMap().getSpots();
        ArrayList<BarrelSpotPair<Barrel, Spot>> barrelSpotPairs = new ArrayList<BarrelSpotPair<Barrel, Spot>>();
        BarrelSpotPair<Barrel, Spot> pair;
        int barrelsSize = barrels.size();

        for (int i = 0; i < barrelsSize; i++) {
            pair = new BarrelSpotPair<Barrel, Spot>(barrels.get(i), spots.get(i));
            barrelSpotPairs.add(pair);
        }
        return barrelSpotPairs;
    }

    protected void solve(int levelNum) {
        try {
            ArrayList<Integer> range = getLoopsRemovalRange();
            Iterator<Integer> it = range.iterator();

            while (it.hasNext()) {
                Integer loopRemoval = it.next();
                Level level = LevelFactory.createFromProperties("level" + levelNum);
                AStar astar = new AStar(level);
                astar.setLoopRemoval(loopRemoval);
                astar.setMaxNumberOfSteps(100000);
                astar.setThreadId(String.valueOf(threadList.size() + 1));
                astar.setObserver(this);
                Thread th = new Thread(astar);
                th.setDaemon(true);
                th.start();
                threadList.add(astar);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected ArrayList<Integer> getLoopsRemovalRange() {
        ArrayList<Integer> range = new ArrayList<Integer>();
        int removeLoopsFrom = Integer.parseInt(loopsRemovalCountFromTextField.getText());
        int removeLoopsTo = Integer.parseInt(loopsRemovalCountToTextField.getText());
        for (int i = removeLoopsFrom; i <= removeLoopsTo; i++) {
            range.add(i);
        }
        return range;
    }

    public void update(final String threadId, final String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (message.equals("END")) {
                    currentThread = threadId;
                    AStar astar = threadList.get(Integer.parseInt(threadId) - 1);
                    showStatsAndNav(astar);
                } else if (message.equals("CANCELLED")) {
                    resetGUIState();
                    statsTableView.setDisable(false);
                }
                String msg = String.format("[%s][Thread %s] %s", DATEFORMAT.format(new Date()), threadId, message);
                statusListView.getItems().add(0, msg);
            }
        });
    }

    public void showStatsAndNav(AStar astar) {
        resetGUIState();
        statsTableView.setDisable(false);
        stepsLabel.setVisible(true);
        stepsPrevButton.setVisible(true);
        stepsNextButton.setVisible(true);
        currentStep = 0;

        currentMoves = astar.getMoves();
        currentMovesSize = currentMoves.size();
        double time = (double) astar.getTimeInterval() / 1000;
        ArrayList<Integer> range = getLoopsRemovalRange();

        drawStep(currentStep);

        ObservableList<StatsParam> stats = FXCollections.observableArrayList(
                new StatsParam("Algorithm:", algorithm.getSelectedToggle().getUserData().equals("astar") ? "A*" : "IDA*"),
                new StatsParam("Found route length:", String.valueOf(currentMovesSize)),
                new StatsParam("Remove loops min:", String.valueOf(range.get(Integer.parseInt(currentThread) - 1))),
                new StatsParam("Winner thread:", currentThread),
                new StatsParam("Number of threads:", String.valueOf(range.size())),
                new StatsParam("Algorithm loops:", String.valueOf(astar.getMovesCount())),
                new StatsParam("Elapsed time:", String.format("%.2fs", time))
        );
        statsTableView.setItems(stats);
        stopAllThreads();
    }

    protected void resetGUIState() {
        progressIndicator.setVisible(false);
        stopButton.setVisible(false);
        statsTableView.setDisable(true);
        solveButton.setDisable(false);
    }

    public void onStepPrevClick(ActionEvent actionEvent) {
        if (currentStep > 0) {
            currentStep--;
        }
        if (currentStep == 0) {
            stepsPrevButton.setDisable(true);
        }
        stepsNextButton.setDisable(false);
        drawStep(currentStep);
    }

    public void onStepNextClick(ActionEvent actionEvent) {
        if (currentStep < currentMovesSize) {
            currentStep++;
        }
        if (currentStep == currentMovesSize) {
            stepsNextButton.setDisable(true);
        }
        stepsPrevButton.setDisable(false);
        drawStep(currentStep);
    }

    public void drawStep(int stepNum) {
        stepsLabel.setText("Step " + stepNum + " of " + currentMovesSize);
        try {
            Level level = LevelFactory.createFromProperties(getCurrentLevelName());
            if (stepNum > 0) {
                for (int i = 0; i < stepNum; i++) {
                    Move move = currentMoves.get(i);
                    level.move(move);
                }
            }
            drawLevel(level);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onStopClick(ActionEvent actionEvent) {
        stopAllThreads();
    }

    protected void stopAllThreads() {
        Iterator<AStar> iterator = threadList.iterator();
        while (iterator.hasNext()) {
            AStar astarThread = iterator.next();
            astarThread.cancel();
        }
        threadList.clear();
    }

    public class StatsParam {
        private String name;
        private String value;

        private StatsParam(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
