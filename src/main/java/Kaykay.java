import java.util.Scanner;
/**
 * Entry point for the Kaykay chatbot.
 */
public class Kaykay {
    private static final String SEPARATOR = "____________________________________________________________";
    private static final String BANNER = "#   #   ###   #   #  #   #   ###   #   #\n"
            + "#  #   #   #   # #   #  #   #   #   # #\n"
            + "###    #####    #    ###    #####    #\n"
            + "#  #   #   #    #    #  #   #   #    #\n"
            + "#   #  #   #    #    #   #  #   #    #";

    /**
     * Greets the user, repeats user input, and exits when user types bye
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        Task[] tasks = new Task[100];
        int taskCount = 0;
        System.out.println(SEPARATOR);
        System.out.println(BANNER);
        System.out.println(SEPARATOR);
        System.out.println("Hello! I'm kaykay.");
        System.out.println("What can I do for you?");
        System.out.println(SEPARATOR);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            try {
                if (input.equals("bye")) {
                    break;
                } else if (input.equals("list")) {
                    System.out.println(SEPARATOR);
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i += 1) {
                        System.out.printf("%d. %s\n", i + 1, tasks[i]);
                    }
                    System.out.println(SEPARATOR);
                } else if (input.startsWith("mark") || input.startsWith("unmark")) {
                    String[] pieces = input.split(" ");
                    if (pieces.length != 2 || !isValidTaskNumber(pieces[1], taskCount)) {
                        throw new KaykayException("Please provide an existing task number to mark or unmark.");
                    }
                    int index = Integer.parseInt(pieces[1]) - 1;
                    System.out.println(SEPARATOR);
                    if (pieces[0].equals("mark")) {
                        System.out.println("Nice! I've marked this task as done:");
                        tasks[index].mark();
                    } else {
                        System.out.println("OK, I've marked this task as not done yet:");
                        tasks[index].unmark();
                    }
                    System.out.println(tasks[index]);
                    System.out.println(SEPARATOR);
                } else if (input.equals("todo") || input.startsWith("todo ")) {
                    String description = input.length() == "todo".length()
                            ? "" : input.substring("todo ".length());
                    if (description.trim().isEmpty()) {
                        throw new KaykayException("A todo needs a description. Try: todo <description>.");
                    }
                    tasks[taskCount++] = new Todo(description);
                    printAddedTask(tasks[taskCount - 1], taskCount);
                } else if (input.equals("deadline") || input.startsWith("deadline ")) {
                    String deadlineInput = input.length() == "deadline".length()
                            ? "" : input.substring("deadline ".length());
                    String[] deadlineParts = deadlineInput.split(" /by ", 2);
                    if (deadlineParts.length == 2 && !deadlineParts[0].trim().isEmpty()
                            && !deadlineParts[1].trim().isEmpty()) {
                        tasks[taskCount++] = new Deadline(deadlineParts[0], deadlineParts[1]);
                        printAddedTask(tasks[taskCount - 1], taskCount);
                    } else {
                        throw new KaykayException("A deadline needs a description and a date. "
                                + "Try: deadline <description> /by <date>.");
                    }
                } else if (input.equals("event") || input.startsWith("event ")) {
                    String eventInput = input.length() == "event".length()
                            ? "" : input.substring("event ".length());
                    String[] fromParts = eventInput.split(" /from ", 2);
                    if (fromParts.length == 2 && !fromParts[0].trim().isEmpty()) {
                        String[] toParts = fromParts[1].split(" /to ", 2);
                        if (toParts.length == 2 && !toParts[0].trim().isEmpty()
                                && !toParts[1].trim().isEmpty()) {
                            tasks[taskCount++] = new Event(fromParts[0], toParts[0], toParts[1]);
                            printAddedTask(tasks[taskCount - 1], taskCount);
                        } else {
                            throw new KaykayException("An event needs a description, start, and end. "
                                    + "Try: event <description> /from <start> /to <end>.");
                        }
                    } else {
                        throw new KaykayException("An event needs a description, start, and end. "
                                + "Try: event <description> /from <start> /to <end>.");
                    }
                } else {
                    throw new KaykayException("I don't recognise that command. Try todo, deadline, event, "
                            + "list, mark, unmark, or bye.");
                }
            } catch (KaykayException exception) {
                printError(exception.getMessage());
            }
        }
        System.out.println(SEPARATOR);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(SEPARATOR);
    }

    /** Prints the standard confirmation after adding a task. */
    private static void printAddedTask(Task task, int taskCount) {
        System.out.println(SEPARATOR);
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        System.out.printf("Now you have %d tasks in the list.\n", taskCount);
        System.out.println(SEPARATOR);
    }

    /** Prints an error surrounded by the chatbot's standard separator. */
    private static void printError(String message) {
        System.out.println(SEPARATOR);
        System.out.println("OOPS! " + message);
        System.out.println(SEPARATOR);
    }

    /** Checks that a mark/unmark argument is an existing positive task number. */
    private static boolean isValidTaskNumber(String value, int taskCount) {
        try {
            int taskNumber = Integer.parseInt(value);
            return taskNumber >= 1 && taskNumber <= taskCount;
        } catch (NumberFormatException exception) {
            return false;
        }
    }
}
