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

import java.util.ArrayList;

/**
 * PSZT
 * Created: 24.11.2013 16:45
 */
public class GameStatusController implements Observer {

    private static final int LEVELS_NUM = 3;
    private static final int TILE_SIZE = 64;
    private final ObservableList<AStar> threadList = FXCollections.observableArrayList();
    public ComboBox levelComboBox;
    public GridPane levelGridPane;
    public Button solveButton;
    public TableView statsTableView;
    public ListView statusListView;
    public ProgressIndicator progressIndicator;
    public TextField loopsRemovalCountTextField;
    public Label stepsLabel;
    public Button stepsPrevButton, stepsNextButton;
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
    }

    public void onLevelChange(ActionEvent actionEvent) {
        levelGridPane.getChildren().clear();
        try {
            drawLevel(LevelFactory.createFromProperties(getCurrentLevelName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        solveButton.setDisable(false);
        statusListView.getItems().clear();
        statusListView.setDisable(true);
        statsTableView.setDisable(true);
        statsTableView.getItems().clear();
        stepsLabel.setVisible(false);
        stepsPrevButton.setVisible(false);
        stepsPrevButton.setDisable(true);
        stepsNextButton.setVisible(false);
        threadList.clear();
    }

    protected String getCurrentLevelName()
    {
        return "level" + (levelComboBox.getSelectionModel().getSelectedIndex() + 1);
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
        solveButton.setDisable(true);
        statusListView.setDisable(false);
        progressIndicator.setVisible(true);
        stepsLabel.setVisible(false);
        stepsPrevButton.setVisible(false);
        stepsPrevButton.setDisable(true);
        stepsNextButton.setVisible(false);
        statusListView.getItems().clear();
        statsTableView.getItems().clear();
        threadList.clear();

        startSolving(levelComboBox.getSelectionModel().getSelectedIndex() + 1);
    }

    protected void startSolving(int levelNum) {
        solve(levelNum, Integer.parseInt(loopsRemovalCountTextField.getText()));
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

    protected void solve(int levelNum, int loopRemoval) {
        try {
            Level level = LevelFactory.createFromProperties("level" + levelNum);

            AStar astar = new AStar(level);
            astar.setLoopRemoval(loopRemoval);
            astar.setMaxNumberOfSteps(100000);
            astar.setThreadId(String.valueOf(threadList.size() + 1));
            astar.setObserver(this);
            new Thread(astar).start();
            threadList.add(astar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(final String threadId, final String message) {
        currentThread = threadId;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (message.equals("END")) {
                    AStar astar = threadList.get(Integer.parseInt(threadId) - 1);
                    showStatsAndNav(astar);
                } else {
                    statusListView.getItems().add(0, message);
                }
            }
        });
    }

    public void showStatsAndNav(AStar astar) {
        progressIndicator.setVisible(false);
        statsTableView.setDisable(false);
        solveButton.setDisable(false);
        stepsLabel.setVisible(true);
        stepsPrevButton.setVisible(true);
        stepsPrevButton.setDisable(true);
        stepsNextButton.setVisible(true);
        stepsNextButton.setDisable(false);
        currentStep = 0;

        currentMoves = astar.getMoves();
        currentMovesSize = currentMoves.size();
        long time = astar.getTimeInterval();

        drawStep(currentStep);

        ObservableList<StatsParam> stats = FXCollections.observableArrayList(
                new StatsParam("Algorithm loops:", String.valueOf(astar.getMovesCount())),
                new StatsParam("Bulldozer moves:", String.valueOf(currentMovesSize)),
                new StatsParam("Elapsed time:", String.valueOf(time) + "ms")
        );
        statsTableView.setItems(stats);
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
                    level.getMovablesMap().execute(move);
                }
            }
            drawLevel(level);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
