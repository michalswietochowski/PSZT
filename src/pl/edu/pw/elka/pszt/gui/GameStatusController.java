package pl.edu.pw.elka.pszt.gui;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import pl.edu.pw.elka.pszt.models.Level;
import pl.edu.pw.elka.pszt.models.Map;
import pl.edu.pw.elka.pszt.models.MovablesMap;
import pl.edu.pw.elka.pszt.utils.LevelFactory;

/**
 * PSZT
 * Created: 24.11.2013 16:45
 */
public class GameStatusController {

    private static final int LEVELS_NUM = 5;
    private static final int TILE_SIZE = 64;
    public ComboBox levelComboBox;
    public GridPane levelGridPane;
    public Button solveButton;
    public TableView statsTableView;
    public ListView statusListView;
    public ProgressBar statusProgressBar;
    protected Level[] levels;

    public GameStatusController() {
        this.levels = new Level[LEVELS_NUM];
        for (int i = 0; i < LEVELS_NUM; i++) {
            try {
                this.levels[i] = LevelFactory.createFromProperties("level" + (i + 1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void initialize() {
        for (int i = 0; i < LEVELS_NUM; i++) {
            levelComboBox.getItems().add(this.levels[i]);
        }
    }

    public void onLevelChange(ActionEvent actionEvent) {
        levelGridPane.getChildren().clear();
        drawLevel((Level) levelComboBox.getValue());
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
        //todo: implement
        this.statsTableView.setDisable(false);
        this.statusListView.getItems().add("Algorithm threads status messages go here...");
        this.statusListView.setDisable(false);
        this.statusProgressBar.setVisible(true);

        Task<Void> task = new Task<Void>() {
            @Override public Void call() {
                for (int i = 1; i <= 100; i++) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    updateProgress(i, 100);
                }
                return null;
            }
        };

        this.statusProgressBar.progressProperty().bind(task.progressProperty());

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }
}
