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
        Parser parser = new Parser();
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

}
