package kiwi.build;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Kiwi kiwi;

    private Image userImage;
    private Image kiwiImage;

    /**
     * Initializes the main window.
     */
    @FXML
    public void initialize() {
        loadImages();
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Loads images from resources or creates placeholders if not found.
     */
    private void loadImages() {
        try {
            userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
        } catch (NullPointerException e) {
            System.out.println("Warning: DaUser.png not found, using placeholder");
            userImage = createPlaceholderImage();
        }
        try {
            kiwiImage = new Image(this.getClass().getResourceAsStream("/images/DaKiwi.png"));
        } catch (NullPointerException e) {
            System.out.println("Warning: DaKiwi.png not found, using placeholder");
            kiwiImage = createPlaceholderImage();
        }
    }

    /**
     * Creates a simple placeholder image.
     *
     * @return a 100x100 gray placeholder image
     */
    private Image createPlaceholderImage() {
        WritableImage image = new WritableImage(100, 100);
        PixelWriter writer = image.getPixelWriter();
        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                writer.setColor(x, y, Color.LIGHTGRAY);
            }
        }
        return image;
    }

    /**
     * Injects the Kiwi instance and displays initial messages.
     *
     * @param k the Kiwi instance
     */
    public void setKiwi(Kiwi k) {
        kiwi = k;

        // Display welcome message
        String welcome = "Hello! I'm Kiwi\nWhat can i do for you?";
        dialogContainer.getChildren().addAll(
                DialogBox.getKiwiDialog(welcome, kiwiImage)
        );

        // Display list of loaded tasks
        String taskList = kiwi.getResponse("list");
        if (!taskList.isEmpty() && !taskList.contains("No tasks")) {
            dialogContainer.getChildren().addAll(
                    DialogBox.getKiwiDialog(taskList, kiwiImage)
            );
        }
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Kiwi's reply
     * and then appends them to the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = kiwi.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getKiwiDialog(response, kiwiImage)
        );
        userInput.clear();
    }
}
