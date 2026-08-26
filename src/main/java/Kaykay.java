import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.io.IOException;
import java.util.ArrayList;
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
        ArrayList<Task> tasks;
        boolean loadFailed = false;
        try {
            tasks = Storage.loadTasks();
        } catch (IOException exception) {
            tasks = new ArrayList<>();
            loadFailed = true;
        }
        System.out.println(SEPARATOR);
        System.out.println(BANNER);
        System.out.println(SEPARATOR);
        System.out.println("Hello! I'm kaykay.");
        System.out.println("What can I do for you?");
        System.out.println(SEPARATOR);
        if (loadFailed) {
            printError("I couldn't load your tasks. Please check the data file.");
        }
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            try {
                if (input.equals("bye")) {
                    break;
                } else if (input.equals("list")) {
                    System.out.println(SEPARATOR);
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i += 1) {
                        System.out.printf("%d. %s\n", i + 1, tasks.get(i));
                    }
                    System.out.println(SEPARATOR);
                } else if (isCommand(input, "delete")) {
                    String[] pieces = input.split("\\s+");
                    if (pieces.length != 2 || !isValidTaskNumber(pieces[1], tasks.size())) {
                        throw new KaykayException("Please provide an existing task number to delete.");
                    }
                    int index = Integer.parseInt(pieces[1]) - 1;
                    Task deletedTask = tasks.remove(index);
                    try {
                        Storage.saveTasks(tasks);
                    } catch (IOException exception) {
                        tasks.add(index, deletedTask);
                        throw exception;
                    }
                    printDeletedTask(deletedTask, tasks.size());
                } else if (isCommand(input, "mark") || isCommand(input, "unmark")) {
                    String[] pieces = input.split("\\s+");
                    if (pieces.length != 2 || !isValidTaskNumber(pieces[1], tasks.size())) {
                        throw new KaykayException("Please provide an existing task number to mark or unmark.");
                    }
                    int index = Integer.parseInt(pieces[1]) - 1;
                    Task changedTask = tasks.get(index);
                    boolean wasDone = changedTask.getStatusIcon().equals("X");
                    if (pieces[0].equals("mark")) {
                        changedTask.mark();
                    } else {
                        changedTask.unmark();
                    }
                    try {
                        Storage.saveTasks(tasks);
                    } catch (IOException exception) {
                        if (wasDone) {
                            changedTask.mark();
                        } else {
                            changedTask.unmark();
                        }
                        throw exception;
                    }
                    System.out.println(SEPARATOR);
                    if (pieces[0].equals("mark")) {
                        System.out.println("Nice! I've marked this task as done:");
                    } else {
                        System.out.println("OK, I've marked this task as not done yet:");
                    }
                    System.out.println(changedTask);
                    System.out.println(SEPARATOR);
                } else if (isCommand(input, "todo")) {
                    String description = input.length() == "todo".length()
                            ? "" : input.substring("todo ".length());
                    if (description.trim().isEmpty()) {
                        throw new KaykayException("A todo needs a description. Try: todo <description>.");
                    }
                    Task addedTask = new Todo(description);
                    tasks.add(addedTask);
                    try {
                        Storage.saveTasks(tasks);
                    } catch (IOException exception) {
                        tasks.remove(addedTask);
                        throw exception;
                    }
                    printAddedTask(addedTask, tasks.size());
                } else if (isCommand(input, "deadline")) {
                    String deadlineInput = input.length() == "deadline".length()
                            ? "" : input.substring("deadline ".length());
                    String[] deadlineParts = deadlineInput.split(" /by ", 2);
                    if (deadlineParts.length == 2 && !deadlineParts[0].trim().isEmpty()
                            && !deadlineParts[1].trim().isEmpty()) {
                        try {
                            LocalDateTime by = DateTimeParser.parse(deadlineParts[1].trim());
                            Task addedTask = new Deadline(deadlineParts[0], by);
                            tasks.add(addedTask);
                            try {
                                Storage.saveTasks(tasks);
                            } catch (IOException exception) {
                                tasks.remove(addedTask);
                                throw exception;
                            }
                            printAddedTask(addedTask, tasks.size());
                        } catch (DateTimeParseException exception) {
                            throw new KaykayException("A deadline date/time must use the format "
                                    + "dd MM yyyy HH:mm.");
                        }
                    } else {
                        throw new KaykayException("A deadline needs a description and a date. "
                                + "Try: deadline <description> /by <date>.");
                    }
                } else if (isCommand(input, "event")) {
                    String eventInput = input.length() == "event".length()
                            ? "" : input.substring("event ".length());
                    String[] fromParts = eventInput.split(" /from ", 2);
                    if (fromParts.length == 2 && !fromParts[0].trim().isEmpty()) {
                        String[] toParts = fromParts[1].split(" /to ", 2);
                        if (toParts.length == 2 && !toParts[0].trim().isEmpty()
                                && !toParts[1].trim().isEmpty()) {
                            try {
                                LocalDateTime from = DateTimeParser.parse(toParts[0].trim());
                                LocalDateTime to = DateTimeParser.parse(toParts[1].trim());
                                Task addedTask = new Event(fromParts[0], from, to);
                                tasks.add(addedTask);
                                try {
                                    Storage.saveTasks(tasks);
                                } catch (IOException exception) {
                                    tasks.remove(addedTask);
                                    throw exception;
                                }
                                printAddedTask(addedTask, tasks.size());
                            } catch (DateTimeParseException exception) {
                                throw new KaykayException("An event date/time must use the format "
                                        + "dd MM yyyy HH:mm.");
                            }
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
                            + "list, delete, mark, unmark, or bye.");
                }
            } catch (KaykayException exception) {
                printError(exception.getMessage());
            } catch (IOException exception) {
                printError("I couldn't save your tasks. Please check the data folder.");
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

    /** Prints the standard confirmation after deleting a task. */
    private static void printDeletedTask(Task task, int taskCount) {
        System.out.println(SEPARATOR);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
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

    /** Checks whether an input is a command or starts with that command and an argument. */
    private static boolean isCommand(String input, String command) {
        return input.equals(command) || input.startsWith(command + " ");
    }
}
