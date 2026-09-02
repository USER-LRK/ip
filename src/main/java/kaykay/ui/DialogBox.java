package kaykay.ui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Displays one message in Kaykay's conversation area.
 *
 * <p>The avatar is represented by a text label for now. This keeps the view
 * self-contained while leaving room for image assets in a later refinement.</p>
 */
public final class DialogBox extends HBox {
    private static final String KAYKAY_AVATAR = "K";
    private static final String USER_AVATAR = "U";
    private static final double AVATAR_WIDTH = 30.0;

    /** Text displayed in the dialog box. */
    @FXML
    private Label text;

    /** Avatar displayed beside the dialog text. */
    @FXML
    private Label avatar;

    /**
     * Creates a dialog box for a message from Kaykay or the user.
     *
     * @param message message to display.
     * @param isUserMessage whether the message came from the user.
     */
    public DialogBox(String message, boolean isUserMessage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load a Kaykay dialog box.", exception);
        }

        avatar.setMinWidth(AVATAR_WIDTH);
        avatar.setText(isUserMessage ? USER_AVATAR : KAYKAY_AVATAR);
        text.setText(message);
        text.setWrapText(true);
        text.getStyleClass().add(isUserMessage ? "user-dialog" : "kaykay-dialog");
        setAlignment(isUserMessage ? Pos.TOP_RIGHT : Pos.TOP_LEFT);
        setSpacing(8.0);
    }

    /**
     * Creates a dialog box aligned as a user message.
     *
     * @param message message to display.
     * @return a user-aligned dialog box.
     */
    public static DialogBox getUserDialog(String message) {
        return new DialogBox(message, true);
    }

    /**
     * Creates a dialog box aligned as a Kaykay message.
     *
     * @param message message to display.
     * @return a Kaykay-aligned dialog box.
     */
    public static DialogBox getKaykayDialog(String message) {
        DialogBox dialogBox = new DialogBox(message, false);
        dialogBox.flip();
        return dialogBox;
    }

    /** Places Kaykay's avatar after the response text. */
    private void flip() {
        setAlignment(Pos.TOP_LEFT);
        getChildren().setAll(avatar, text);
    }
}
