package kaykay.command;

import java.io.IOException;

import kaykay.model.Task;
import kaykay.model.TaskList;
import kaykay.storage.Storage;
import kaykay.ui.Ui;

/**
 * Provides shared save-and-confirm behavior for task-creation commands.
 */
public abstract class AddCommand extends Command {
    /** Description shared by the command and the task it creates. */
    private final String description;

    /** Creates an add command for a task description. */
    protected AddCommand(String description) {
        this.description = description;
    }

    /** Creates the concrete task represented by this command. */
    protected abstract Task createTask();

    /** @return the description supplied to this command */
    protected String getDescription() {
        return description;
    }

    @Override
    public final void execute(TaskList tasks, Ui ui, Storage storage) throws IOException {
        Task addedTask = createTask();
        tasks.add(addedTask);
        try {
            storage.saveTasks(tasks);
        } catch (IOException exception) {
            tasks.remove(addedTask);
            throw exception;
        }
        ui.showAddedTask(addedTask, tasks.size());
    }
}
