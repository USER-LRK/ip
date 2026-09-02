package kaykay.ui;

import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

import kaykay.model.Task;
import kaykay.model.TaskList;

/**
 * Handles all console input and output for the Kaykay chatbot.
 */
public class Ui {
    private static final String SEPARATOR = "____________________________________________________________";
    private static final String BANNER = "#   #   ###   #   #  #   #   ###   #   #\n"
            + "#  #   #   #   # #   #  #   #   #   # #\n"
            + "###    #####    #    ###    #####    #\n"
            + "#  #   #   #    #    #  #   #   #    #\n"
            + "#   #  #   #    #    #   #  #   #    #";

    /** Reads commands typed by the user. */
    private final Scanner scanner;

    /** Receives rendered output lines from the UI. */
    private final Consumer<String> output;

    /** Creates a UI that reads from standard input and writes to standard output. */
    public Ui() {
        this(System.out::println);
    }

    /**
     * Creates a UI that reads from standard input and sends output to a consumer.
     *
     * @param output receiver for rendered output lines.
     */
    public Ui(Consumer<String> output) {
        scanner = new Scanner(System.in);
        this.output = output;
    }

    /**
     * Checks whether another input line is available.
     *
     * @return true if the user or input stream has another line.
     */
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    /**
     * Reads and trims one command from standard input.
     *
     * @return the next command without leading or trailing whitespace.
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /** Shows Kaykay's greeting. */
    public void showWelcome() {
        showLine(SEPARATOR);
        showLine(BANNER);
        showLine(SEPARATOR);
        showLine("Hello! I'm kaykay.");
        showLine("What can I do for you?");
        showLine(SEPARATOR);
    }

    /** Shows the message used when saved tasks cannot be loaded. */
    public void showLoadingError() {
        showError("I couldn't load your tasks. Please check the data file.");
    }

    /**
     * Shows all tasks with their one-based positions.
     *
     * @param tasks tasks to display.
     */
    public void showTaskList(TaskList tasks) {
        showLine(SEPARATOR);
        showLine("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i += 1) {
            showLine(String.format("%d. %s", i + 1, tasks.getTask(i)));
        }
        showLine(SEPARATOR);
    }

    /**
     * Shows matching tasks with positions relative to the search results.
     *
     * @param matchingTasks tasks that matched the user's search keyword.
     */
    public void showMatchingTasks(List<Task> matchingTasks) {
        showLine(SEPARATOR);
        showLine("Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i += 1) {
            showLine(String.format("%d. %s", i + 1, matchingTasks.get(i)));
        }
        showLine(SEPARATOR);
    }

    /**
     * Shows the standard confirmation after adding a task.
     *
     * @param task task that was added.
     * @param taskCount number of tasks after the addition.
     */
    public void showAddedTask(Task task, int taskCount) {
        showLine(SEPARATOR);
        showLine("Got it. I've added this task:");
        showLine(task.toString());
        showLine(String.format("Now you have %d tasks in the list.", taskCount));
        showLine(SEPARATOR);
    }

    /**
     * Shows the standard confirmation after deleting a task.
     *
     * @param task task that was deleted.
     * @param taskCount number of tasks after the deletion.
     */
    public void showDeletedTask(Task task, int taskCount) {
        showLine(SEPARATOR);
        showLine("Noted. I've removed this task:");
        showLine("  " + task);
        showLine(String.format("Now you have %d tasks in the list.", taskCount));
        showLine(SEPARATOR);
    }

    /**
     * Shows the standard confirmation after marking or unmarking a task.
     *
     * @param task task whose status changed.
     * @param marked whether the task was marked as done.
     */
    public void showMarkedTask(Task task, boolean marked) {
        showLine(SEPARATOR);
        if (marked) {
            showLine("Nice! I've marked this task as done:");
        } else {
            showLine("OK, I've marked this task as not done yet:");
        }
        showLine(task.toString());
        showLine(SEPARATOR);
    }

    /**
     * Shows an error surrounded by the chatbot's standard separator.
     *
     * @param message error message to display.
     */
    public void showError(String message) {
        showLine(SEPARATOR);
        showLine("OOPS! " + message);
        showLine(SEPARATOR);
    }

    /** Shows Kaykay's farewell. */
    public void showFarewell() {
        showLine(SEPARATOR);
        showLine("Bye. Hope to see you again soon!");
        showLine(SEPARATOR);
    }

    /** Sends one rendered line to the configured output receiver. */
    private void showLine(String line) {
        output.accept(line);
    }
}
