package pl.edu.pw.elka.pszt.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
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
public class GameStatusController {

    private static final int LEVELS_NUM = 5;
    private static final int TILE_SIZE = 64;
    private final ObservableList<AStar> threadList = FXCollections.observableArrayList();
    public ComboBox levelComboBox;
    public GridPane levelGridPane;
    public Button solveButton;
    public TableView statsTableView;
    public ListView statusListView;
    public ProgressBar statusProgressBar;
    public CheckBox initMoveNCheckbox, initMoveSCheckbox, initMoveECheckbox, initMoveWCheckbox;

    public void initialize() {
        for (int i = 1; i <= LEVELS_NUM; i++) {
            levelComboBox.getItems().add("Level " + i);
        }
    }

    public void onLevelChange(ActionEvent actionEvent) {
        levelGridPane.getChildren().clear();
        try {
            drawLevel(LevelFactory.createFromProperties("level" + (levelComboBox.getSelectionModel().getSelectedIndex() + 1)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        solveButton.setDisable(false);
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
        statsTableView.setDisable(false);
        statusListView.setDisable(false);
        statusProgressBar.setVisible(true);
        statusListView.getItems().clear();
        threadList.clear();

        startSolving(levelComboBox.getSelectionModel().getSelectedIndex() + 1);
    }

    protected void startSolving(int levelNum) {
        if (initMoveNCheckbox.isSelected()) {
            solve(levelNum, "N");
        }
        if (initMoveSCheckbox.isSelected()) {
            solve(levelNum, "S");
        }
        if (initMoveWCheckbox.isSelected()) {
            solve(levelNum, "W");
        }
        if (initMoveECheckbox.isSelected()) {
            solve(levelNum, "E");
        }
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

    protected void solve(int levelNum, String move) {
        try {
            Level level = LevelFactory.createFromProperties("level" + levelNum);
            int[] bc = level.getMovablesMap().findBulldozer();
            Move initialMove = null;

            if (move.equals("N")) {
                initialMove = new Move(bc[0], bc[1] + 1, bc[0], bc[1]);
            } else if (move.equals("S")) {
                initialMove = new Move(bc[0], bc[1] - 1, bc[0], bc[1]);
            } else if (move.equals("W")) {
                initialMove = new Move(bc[0] + 1, bc[1], bc[0], bc[1]);
            } else if (move.equals("E")) {
                initialMove = new Move(bc[0] - 1, bc[1], bc[0], bc[1]);
            }

            AStar astar = new AStar(level);
            astar.setBarrelSpotPairs(getBarrelSpotPair(level));
            astar.setInitialMove(initialMove);
            astar.setThreadId(move);
            astar.messageProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                    update(observableValue.getValue());
                }
            });
            new Thread(astar).start();
            threadList.add(astar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(String s) {
        statusListView.getItems().add(0, s);
    }
}
