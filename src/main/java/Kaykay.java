import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.io.IOException;
/**
 * Entry point for the Kaykay chatbot.
 */
public class Kaykay {
    /**
     * Greets the user, repeats user input, and exits when user types bye
     *
    * @param args command-line arguments, which are not used
    */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage("data/kaykay.txt");
        TaskList tasks;
        boolean loadFailed = false;
        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (IOException exception) {
            tasks = new TaskList();
            loadFailed = true;
        }
        ui.showWelcome();
        if (loadFailed) {
            ui.showLoadingError();
        }
        while (ui.hasNextLine()) {
            String input = ui.readCommand();
            try {
                if (input.equals("bye")) {
                    break;
                } else if (input.equals("list")) {
                    ui.showTaskList(tasks);
                } else if (isCommand(input, "delete")) {
                    String[] pieces = input.split("\\s+");
                    if (pieces.length != 2 || !tasks.isValidTaskNumber(pieces[1])) {
                        throw new KaykayException("Please provide an existing task number to delete.");
                    }
                    int index = Integer.parseInt(pieces[1]) - 1;
                    Task deletedTask = tasks.remove(index);
                    try {
                        storage.saveTasks(tasks);
                    } catch (IOException exception) {
                        tasks.add(index, deletedTask);
                        throw exception;
                    }
                    ui.showDeletedTask(deletedTask, tasks.size());
                } else if (isCommand(input, "mark") || isCommand(input, "unmark")) {
                    String[] pieces = input.split("\\s+");
                    if (pieces.length != 2 || !tasks.isValidTaskNumber(pieces[1])) {
                        throw new KaykayException("Please provide an existing task number to mark or unmark.");
                    }
                    int index = Integer.parseInt(pieces[1]) - 1;
                    Task changedTask = tasks.getTask(index);
                    boolean wasDone = changedTask.getStatusIcon().equals("X");
                    if (pieces[0].equals("mark")) {
                        changedTask.mark();
                    } else {
                        changedTask.unmark();
                    }
                    try {
                        storage.saveTasks(tasks);
                    } catch (IOException exception) {
                        if (wasDone) {
                            changedTask.mark();
                        } else {
                            changedTask.unmark();
                        }
                        throw exception;
                    }
                    ui.showMarkedTask(changedTask, pieces[0].equals("mark"));
                } else if (isCommand(input, "todo")) {
                    String description = input.length() == "todo".length()
                            ? "" : input.substring("todo ".length());
                    if (description.trim().isEmpty()) {
                        throw new KaykayException("A todo needs a description. Try: todo <description>.");
                    }
                    Task addedTask = new Todo(description);
                    tasks.add(addedTask);
                    try {
                        storage.saveTasks(tasks);
                    } catch (IOException exception) {
                        tasks.remove(addedTask);
                        throw exception;
                    }
                    ui.showAddedTask(addedTask, tasks.size());
                } else if (isCommand(input, "deadline")) {
                    String deadlineInput = input.length() == "deadline".length()
                            ? "" : input.substring("deadline ".length());
                    String[] deadlineParts = deadlineInput.split(" /by ", 2);
                    if (deadlineParts.length == 2 && !deadlineParts[0].trim().isEmpty()
                            && !deadlineParts[1].trim().isEmpty()) {
                        String byText = deadlineParts[1].trim();
                        try {
                            LocalDateTime by = DateTimeParser.parse(byText);
                            Task addedTask = new Deadline(deadlineParts[0], by);
                            tasks.add(addedTask);
                            try {
                                storage.saveTasks(tasks);
                            } catch (IOException exception) {
                                tasks.remove(addedTask);
                                throw exception;
                            }
                            ui.showAddedTask(addedTask, tasks.size());
                        } catch (DateTimeParseException exception) {
                            throw invalidDateTime("deadline", byText);
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
                            String fromText = toParts[0].trim();
                            String toText = toParts[1].trim();
                            LocalDateTime from;
                            LocalDateTime to;
                            try {
                                from = DateTimeParser.parse(fromText);
                            } catch (DateTimeParseException exception) {
                                throw invalidDateTime("event start", fromText);
                            }
                            try {
                                to = DateTimeParser.parse(toText);
                            } catch (DateTimeParseException exception) {
                                throw invalidDateTime("event end", toText);
                            }
                            Task addedTask = new Event(fromParts[0], from, to);
                            tasks.add(addedTask);
                            try {
                                storage.saveTasks(tasks);
                            } catch (IOException exception) {
                                tasks.remove(addedTask);
                                throw exception;
                            }
                            ui.showAddedTask(addedTask, tasks.size());
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
                ui.showError(exception.getMessage());
            } catch (IOException exception) {
                ui.showError("I couldn't save your tasks. Please check the data folder.");
            }
        }
        ui.showFarewell();
    }

    /** Builds a date/time error that identifies the invalid input and its expected format. */
    private static KaykayException invalidDateTime(String field, String value) {
        return new KaykayException(String.format(
                "The %s date/time '%s' is invalid. Please use %s, for example %s.",
                field, value, DateTimeParser.INPUT_FORMAT, DateTimeParser.EXAMPLE));
    }

    /** Checks whether an input is a command or starts with that command and an argument. */
    private static boolean isCommand(String input, String command) {
        return input.equals(command) || input.startsWith(command + " ");
    }
}
