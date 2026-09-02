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

/**
 * Provides the initial JavaFX application window for Kaykay.
 *
 * <p>This class owns the JavaFX scene and its top-level controls. User input
 * handling will be added in the next increment.</p>
 */
public final class Main extends Application {
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

    /** Will submit the command once input handling is added. */
    private Button sendButton;

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

        dialogContainer.getChildren().add(new DialogBox("Hello! I'm kaykay.", false));

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

        stage.show();
    }
}
