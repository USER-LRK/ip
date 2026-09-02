package kaykay.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import kaykay.Kaykay;

/**
 * Controls Kaykay's main JavaFX window defined in {@code MainWindow.fxml}.
 */
public final class MainWindow extends AnchorPane {
    private static final String DEFAULT_FILE_PATH = "data/kaykay.txt";

    /** Displays the conversation messages. */
    @FXML
    private ScrollPane scrollPane;

    /** Contains the conversation's dialog boxes. */
    @FXML
    private VBox dialogContainer;

    /** Accepts a command from the user. */
    @FXML
    private TextField userInput;

    /** Submits the command entered by the user. */
    @FXML
    private Button sendButton;

    /** Buffers rendered Kaykay output before it is shown in one dialog. */
    private final StringBuilder responseBuffer = new StringBuilder();

    /** Renders chatbot output into the response buffer. */
    private final Ui ui = new Ui(this::captureResponse, false);

    /** Processes commands and manages the task data. */
    private final Kaykay kaykay = new Kaykay(DEFAULT_FILE_PATH, ui);

    /** Initializes the controls injected from the FXML view. */
    @FXML
    private void initialize() {
        dialogContainer.getChildren().add(DialogBox.getKaykayDialog("Hello! I'm kaykay."));
        dialogContainer.heightProperty().addListener((observable, oldHeight, newHeight) ->
                scrollPane.setVvalue(1.0));
    }

    /** Processes the entered command and appends the resulting dialog boxes. */
    @FXML
    private void handleUserInput() {
        String userText = userInput.getText().trim();
        dialogContainer.getChildren().add(DialogBox.getUserDialog(userText));

        boolean isExit = kaykay.processCommand(userText);
        if (isExit) {
            ui.showFarewell();
        }

        String kaykayText = takeResponse();
        if (!kaykayText.isEmpty()) {
            dialogContainer.getChildren().add(DialogBox.getKaykayDialog(kaykayText));
        }
        userInput.clear();

        if (isExit) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
        }
    }

    /** Captures one rendered UI line for the current chatbot response. */
    private void captureResponse(String line) {
        responseBuffer.append(line).append(System.lineSeparator());
    }

    /** Returns the buffered response and clears it for the next command. */
    private String takeResponse() {
        String response = responseBuffer.toString().stripTrailing();
        responseBuffer.setLength(0);
        return response;
    }
}
