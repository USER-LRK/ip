package kaykay.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Displays one message in Kaykay's conversation area.
 *
 * <p>The avatar is represented by a text label for now. This keeps the Part 2
 * layout self-contained while leaving room for image assets in a later
 * refinement.</p>
 */
public final class DialogBox extends HBox {
    private static final String KAYKAY_AVATAR = "K";
    private static final String USER_AVATAR = "U";
    private static final double AVATAR_WIDTH = 30.0;

    /**
     * Creates a dialog box for a message from Kaykay or the user.
     *
     * @param message message to display.
     * @param isUserMessage whether the message came from the user.
     */
    public DialogBox(String message, boolean isUserMessage) {
        Label avatar = new Label(isUserMessage ? USER_AVATAR : KAYKAY_AVATAR);
        Label text = new Label(message);

        avatar.setMinWidth(AVATAR_WIDTH);
        text.setWrapText(true);
        setAlignment(isUserMessage ? Pos.TOP_RIGHT : Pos.TOP_LEFT);
        setSpacing(8.0);
        getChildren().addAll(avatar, text);
    }
}
