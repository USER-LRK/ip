package kaykay;

import java.io.IOException;

import kaykay.command.Command;
import kaykay.exception.KaykayException;
import kaykay.model.TaskList;
import kaykay.parser.Parser;
import kaykay.storage.Storage;
import kaykay.ui.Ui;

/**
 * Entry point and coordinator for the Kaykay chatbot.
 */
public class Kaykay {
    /** Provides console input and output. */
    private final Ui ui;

    /** Loads and saves the chatbot's tasks. */
    private final Storage storage;

    /** Holds the tasks managed during this run. */
    private final TaskList tasks;

    /** Interprets each line of user input. */
    private final Parser parser;

    /** Whether loading the initial task data failed. */
    private final boolean loadFailed;

    /**
     * Creates a Kaykay chatbot using the given task data file.
     *
     * @param filePath path of the task data file
     */
    public Kaykay(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        parser = new Parser();
        TaskList loadedTasks;
        boolean failedToLoad;
        try {
            loadedTasks = new TaskList(storage.loadTasks());
            failedToLoad = false;
        } catch (IOException exception) {
            loadedTasks = new TaskList();
            failedToLoad = true;
        }
        tasks = loadedTasks;
        loadFailed = failedToLoad;
    }

    /** Runs the chatbot until the user says bye or input ends. */
    public void run() {
        ui.showWelcome();
        if (loadFailed) {
            ui.showLoadingError();
        }
        while (ui.hasNextLine()) {
            String input = ui.readCommand();
            try {
                Command command = parser.parse(input);
                command.execute(tasks, ui, storage);
                if (command.isExit()) {
                    break;
                }
            } catch (KaykayException exception) {
                ui.showError(exception.getMessage());
            } catch (IOException exception) {
                ui.showError("I couldn't save your tasks. Please check the data folder.");
            }
        }
        ui.showFarewell();
    }

    /**
     * Starts Kaykay with its default task data file.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        new Kaykay("data/kaykay.txt").run();
    }
}
