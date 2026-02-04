/**
 * JavaFX GUI entry point for Kiwi task manager application.
 * Integrates with existing Kiwi CLI logic for Level-10 requirement.
 */
package kiwi.build;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Main JavaFX application class that loads the GUI and initializes Kiwi logic.
 */
public class Main extends Application {

    private Kiwi kiwi = new Kiwi();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("Kiwi - Task Manager");
            fxmlLoader.<MainWindow>getController().setKiwi(kiwi);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
