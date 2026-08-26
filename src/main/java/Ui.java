import java.util.List;
import java.util.Scanner;

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

    /** Creates a UI that reads from standard input and writes to standard output. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Checks whether another input line is available.
     *
     * @return true if the user or input stream has another line
     */
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    /**
     * Reads and trims one command from standard input.
     *
     * @return the next command without leading or trailing whitespace
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /** Shows Kaykay's greeting. */
    public void showWelcome() {
        System.out.println(SEPARATOR);
        System.out.println(BANNER);
        System.out.println(SEPARATOR);
        System.out.println("Hello! I'm kaykay.");
        System.out.println("What can I do for you?");
        System.out.println(SEPARATOR);
    }

    /** Shows the message used when saved tasks cannot be loaded. */
    public void showLoadingError() {
        showError("I couldn't load your tasks. Please check the data file.");
    }

    /** Shows all tasks with their one-based positions. */
    public void showTaskList(List<Task> tasks) {
        System.out.println(SEPARATOR);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i += 1) {
            System.out.printf("%d. %s\n", i + 1, tasks.get(i));
        }
        System.out.println(SEPARATOR);
    }

    /** Shows the standard confirmation after adding a task. */
    public void showAddedTask(Task task, int taskCount) {
        System.out.println(SEPARATOR);
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        System.out.printf("Now you have %d tasks in the list.\n", taskCount);
        System.out.println(SEPARATOR);
    }

    /** Shows the standard confirmation after deleting a task. */
    public void showDeletedTask(Task task, int taskCount) {
        System.out.println(SEPARATOR);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.printf("Now you have %d tasks in the list.\n", taskCount);
        System.out.println(SEPARATOR);
    }

    /** Shows the standard confirmation after marking or unmarking a task. */
    public void showMarkedTask(Task task, boolean marked) {
        System.out.println(SEPARATOR);
        if (marked) {
            System.out.println("Nice! I've marked this task as done:");
        } else {
            System.out.println("OK, I've marked this task as not done yet:");
        }
        System.out.println(task);
        System.out.println(SEPARATOR);
    }

    /** Shows an error surrounded by the chatbot's standard separator. */
    public void showError(String message) {
        System.out.println(SEPARATOR);
        System.out.println("OOPS! " + message);
        System.out.println(SEPARATOR);
    }

    /** Shows Kaykay's farewell. */
    public void showFarewell() {
        System.out.println(SEPARATOR);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(SEPARATOR);
    }
}
