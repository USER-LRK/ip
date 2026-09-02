package kaykay.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Provides the initial JavaFX application window for Kaykay.
 *
 * <p>This first scaffold establishes the JavaFX application lifecycle. Later
 * increments will replace the placeholder label with Kaykay's conversation UI.</p>
 */
public final class Main extends Application {
    private static final String WINDOW_TITLE = "Kaykay";
    private static final String PLACEHOLDER_MESSAGE = "Hello from Kaykay!";

    /**
     * Starts the JavaFX application window.
     *
     * @param stage primary stage supplied by JavaFX.
     */
    @Override
    public void start(Stage stage) {
        Label greeting = new Label(PLACEHOLDER_MESSAGE);
        Scene scene = new Scene(greeting);

        stage.setTitle(WINDOW_TITLE);
        stage.setScene(scene);
        stage.show();
    }
}
