package kiwi.build;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {

    private static final int AVATAR_SIZE = 80;

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
        setBackgroundImage();

        // Make ScrollPane transparent
        scrollPane.setStyle(
            "-fx-background-color: transparent !important;"
            + "-fx-background: transparent !important;"
            + "-fx-control-inner-background: transparent !important;"
        );

        // VBox should grow to fill available space
        VBox.setVgrow(dialogContainer, javafx.scene.layout.Priority.ALWAYS);

        // Auto-scroll to bottom when new content is added
        dialogContainer.heightProperty().addListener((obs, oldVal, newVal) -> {
            scrollPane.setVvalue(1.0);
        });
    }


    /**
     * Sets the background image for the main window.
     * Image should be located at resources/images/background.png
     * Background fills the AnchorPane (entire window)
     */
    private void setBackgroundImage() {
        try {
            String imagePath = "/images/background.png";
            Image backgroundImg = new Image(this.getClass().getResourceAsStream(imagePath));

            if (backgroundImg.isError()) {
                System.out.println("ERROR loading background: " + backgroundImg.getException());
                return;
            }

            System.out.println("LOADED: " + imagePath + " ("
                + backgroundImg.getWidth() + "x" + backgroundImg.getHeight() + ")");

            BackgroundImage bgImage = new BackgroundImage(
                backgroundImg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(400, 600, false, false, false, false)
            );

            // Apply background to the main AnchorPane (entire window)
            this.setBackground(new Background(bgImage));

            // Make dialogContainer transparent so background shows
            dialogContainer.setStyle("-fx-background-color: transparent;");

            System.out.println("DEBUG: Background set on main AnchorPane");

        } catch (Exception e) {
            System.out.println("EXCEPTION in setBackgroundImage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads images from resources or creates placeholders if not found.
     */
    private void loadImages() {
        try {
            userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
            if (userImage.isError()) {
                throw new NullPointerException("Image error");
            }
            System.out.println("User image loaded successfully");
        } catch (Exception e) {
            System.out.println("Warning: DaUser.png not found, using placeholder");
            userImage = createPlaceholderImage();
        }

        try {
            kiwiImage = new Image(this.getClass().getResourceAsStream("/images/DaKiwi.png"));
            if (kiwiImage.isError()) {
                throw new NullPointerException("Image error");
            }
            System.out.println("Kiwi image loaded successfully");
        } catch (Exception e) {
            System.out.println("Warning: DaKiwi.png not found, using placeholder");
            kiwiImage = createPlaceholderImage();
        }
    }

    /**
     * Creates a simple placeholder image.
     *
     * @return a 80x80 gray placeholder image
     */
    private Image createPlaceholderImage() {
        WritableImage image = new WritableImage(AVATAR_SIZE, AVATAR_SIZE);
        PixelWriter writer = image.getPixelWriter();
        for (int x = 0; x < AVATAR_SIZE; x++) {
            for (int y = 0; y < AVATAR_SIZE; y++) {
                writer.setColor(x, y, Color.LIGHTGRAY);
            }
        }
        System.out.println("Placeholder image created (80x80)");
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
        String welcome = "Hello! I'm Kiwi\nWhat can I do for you?";
        dialogContainer.getChildren().addAll(
                DialogBox.getKiwiDialog(welcome, kiwiImage)
        );

        // Display list of loaded tasks
        String taskList = kiwi.getResponse("list");
        if (!taskList.isEmpty() && !taskList.contains("No matching tasks")) {
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
        if (input.trim().isEmpty()) {
            return;
        }

        String response = kiwi.getResponse(input);

        var userDialog = DialogBox.getUserDialog(input, userImage);
        userDialog.getStyleClass().add("user-dialog");

        var kiwiDialog = DialogBox.getKiwiDialog(response, kiwiImage);
        kiwiDialog.getStyleClass().add("kiwi-dialog");

        dialogContainer.getChildren().addAll(userDialog, kiwiDialog);
        userInput.clear();
    }
}
