package kaykay.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kaykay.Kaykay;

/**
 * Provides the initial JavaFX application window for Kaykay.
 *
 * <p>This class owns the JavaFX scene and connects its controls to Kaykay's
 * command-processing logic.</p>
 */
public final class Main extends Application {
    private static final String DEFAULT_FILE_PATH = "data/kaykay.txt";
    private static final String WINDOW_TITLE = "Kaykay";
    private static final double WINDOW_WIDTH = 400.0;
    private static final double WINDOW_HEIGHT = 600.0;
    private static final double SCROLL_PANE_WIDTH = 385.0;
    private static final double SCROLL_PANE_HEIGHT = 535.0;
    private static final double INPUT_WIDTH = 325.0;
    private static final double SEND_BUTTON_WIDTH = 55.0;

    /** Displays the conversation messages. */
    private ScrollPane scrollPane;

    /** Contains the conversation's dialog boxes. */
    private VBox dialogContainer;

    /** Accepts a command from the user. */
    private TextField userInput;

    /** Submits the command entered by the user. */
    private Button sendButton;

    /** Buffers rendered Kaykay output before it is shown in one dialog. */
    private final StringBuilder responseBuffer = new StringBuilder();

    /** Renders chatbot output into the response buffer. */
    private final Ui ui = new Ui(this::captureResponse);

    /** Processes commands and manages the task data. */
    private final Kaykay kaykay = new Kaykay(DEFAULT_FILE_PATH, ui);

    /**
     * Starts the JavaFX application window.
     *
     * @param stage primary stage supplied by JavaFX.
     */
    @Override
    public void start(Stage stage) {
        scrollPane = new ScrollPane();
        dialogContainer = new VBox(10);
        dialogContainer.setPadding(new Insets(10));
        scrollPane.setContent(dialogContainer);

        userInput = new TextField();
        sendButton = new Button("Send");

        dialogContainer.getChildren().add(DialogBox.getKaykayDialog("Hello! I'm kaykay."));

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        Scene scene = new Scene(mainLayout);

        stage.setTitle(WINDOW_TITLE);
        stage.setScene(scene);

        stage.setResizable(false);
        stage.setMinHeight(WINDOW_HEIGHT);
        stage.setMinWidth(WINDOW_WIDTH);

        mainLayout.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        scrollPane.setPrefSize(SCROLL_PANE_WIDTH, SCROLL_PANE_HEIGHT);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setFitToWidth(true);

        dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);
        userInput.setPrefWidth(INPUT_WIDTH);
        sendButton.setPrefWidth(SEND_BUTTON_WIDTH);

        AnchorPane.setTopAnchor(scrollPane, 1.0);
        AnchorPane.setBottomAnchor(sendButton, 1.0);
        AnchorPane.setRightAnchor(sendButton, 1.0);
        AnchorPane.setLeftAnchor(userInput, 1.0);
        AnchorPane.setBottomAnchor(userInput, 1.0);

        sendButton.setOnAction(event -> handleUserInput());
        userInput.setOnAction(event -> handleUserInput());
        dialogContainer.heightProperty().addListener((observable, oldHeight, newHeight) ->
                scrollPane.setVvalue(1.0));

        stage.show();
    }

    /** Processes the entered command and appends the resulting dialog boxes. */
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
