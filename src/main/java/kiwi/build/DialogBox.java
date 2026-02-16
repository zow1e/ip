/**
 * Custom dialog box for chat messages with user/Kiwi avatar support.
 *
 * Displays messages with avatars in a consistent, fixed size format.
 * Both user and Kiwi avatars are rendered at 80x80 pixels.
 */
package kiwi.build;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a dialog box with a speaker's avatar and message text.
 */
public class DialogBox extends HBox {

    private static final int AVATAR_SIZE = 80;

    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    /**
     * Constructs a DialogBox with the given text and image.
     *
     * @param text the text to display
     * @param img the image to display
     */
    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            getChildren().addAll(new ImageView(img), new Label(text));
            return;
        }

        if (dialog != null) {
            dialog.setText(text);
            dialog.setMaxWidth(250.0);
        }

        if (displayPicture != null) {
            displayPicture.setImage(img);
            setAvatarSize();
        }
    }

    /**
     * Sets consistent dimensions for the avatar image.
     * Both user and Kiwi avatars are sized to 80x80 pixels.
     */
    private void setAvatarSize() {
        displayPicture.setFitHeight(AVATAR_SIZE);
        displayPicture.setFitWidth(AVATAR_SIZE);
        displayPicture.setPreserveRatio(true);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     * Used for user messages to align them to the right side.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        this.getChildren().setAll(tmp);
        setAlignment(Pos.TOP_RIGHT);
    }

    /**
     * Creates a dialog box for user messages.
     * User messages appear right-aligned with avatar on the right.
     *
     * @param text the user's message
     * @param img the user's avatar
     * @return a DialogBox for user messages
     */
    public static DialogBox getUserDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
        return db;
    }

    /**
     * Creates a dialog box for Kiwi's responses.
     * Kiwi messages appear left-aligned with avatar on the left (default order).
     *
     * @param text Kiwi's response
     * @param img Kiwi's avatar
     * @return a DialogBox for Kiwi's messages
     */
    public static DialogBox getKiwiDialog(String text, Image img) {
        return new DialogBox(text, img);
    }
}
