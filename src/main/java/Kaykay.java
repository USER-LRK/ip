import java.io.IOException;
/**
 * Entry point for the Kaykay chatbot.
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
                Parser.Command command = parser.parse(input);
                if (command.getType() == Parser.CommandType.BYE) {
                    break;
                } else if (command.getType() == Parser.CommandType.LIST) {
                    ui.showTaskList(tasks);
                } else if (command.getType() == Parser.CommandType.DELETE) {
                    if (!tasks.isValidTaskNumber(command.getValue())) {
                        throw new KaykayException("Please provide an existing task number to delete.");
                    }
                    int index = Integer.parseInt(command.getValue()) - 1;
                    Task deletedTask = tasks.remove(index);
                    try {
                        storage.saveTasks(tasks);
                    } catch (IOException exception) {
                        tasks.add(index, deletedTask);
                        throw exception;
                    }
                    ui.showDeletedTask(deletedTask, tasks.size());
                } else if (command.getType() == Parser.CommandType.MARK
                        || command.getType() == Parser.CommandType.UNMARK) {
                    if (!tasks.isValidTaskNumber(command.getValue())) {
                        throw new KaykayException("Please provide an existing task number to mark or unmark.");
                    }
                    int index = Integer.parseInt(command.getValue()) - 1;
                    Task changedTask = tasks.getTask(index);
                    boolean wasDone = changedTask.getStatusIcon().equals("X");
                    boolean marked = command.getType() == Parser.CommandType.MARK;
                    if (marked) {
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
                    ui.showMarkedTask(changedTask, marked);
                } else if (command.getType() == Parser.CommandType.TODO) {
                    Task addedTask = new Todo(command.getValue());
                    tasks.add(addedTask);
                    try {
                        storage.saveTasks(tasks);
                    } catch (IOException exception) {
                        tasks.remove(addedTask);
                        throw exception;
                    }
                    ui.showAddedTask(addedTask, tasks.size());
                } else if (command.getType() == Parser.CommandType.DEADLINE) {
                    Task addedTask = new Deadline(command.getValue(), command.getStart());
                    tasks.add(addedTask);
                    try {
                        storage.saveTasks(tasks);
                    } catch (IOException exception) {
                        tasks.remove(addedTask);
                        throw exception;
                    }
                    ui.showAddedTask(addedTask, tasks.size());
                } else if (command.getType() == Parser.CommandType.EVENT) {
                    Task addedTask = new Event(command.getValue(), command.getStart(), command.getEnd());
                    tasks.add(addedTask);
                    try {
                        storage.saveTasks(tasks);
                    } catch (IOException exception) {
                        tasks.remove(addedTask);
                        throw exception;
                    }
                    ui.showAddedTask(addedTask, tasks.size());
                }
            } catch (KaykayException exception) {
                ui.showError(exception.getMessage());
            } catch (IOException exception) {
                ui.showError("I couldn't save your tasks. Please check the data folder.");
            }
        }
        ui.showFarewell();
    }

    /** Starts Kaykay with its default task data file. */
    public static void main(String[] args) {
        new Kaykay("data/kaykay.txt").run();
    }
}
