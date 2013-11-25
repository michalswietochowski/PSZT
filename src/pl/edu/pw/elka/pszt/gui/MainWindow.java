package pl.edu.pw.elka.pszt.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * PSZT
 * Created: 24.11.2013 16:33
 */
public class MainWindow extends Application implements Runnable {
    private static final String WINDOW_TITLE = "Socoban Solver";

    private static final int WINDOW_HEIGHT = 768;
    private static final int WINDOW_WIDTH = 1024;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("GameStatus.fxml"));
        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.show();
    }

    @Override
    public void run() {
        launch();
    }
}
