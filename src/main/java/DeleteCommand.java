import java.io.IOException;

/**
 * Deletes a task and persists the resulting task list.
 */
public final class DeleteCommand extends Command {
    /** User-supplied one-based task number. */
    private final String taskNumber;

    /** Creates a delete command for a task number. */
    public DeleteCommand(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KaykayException, IOException {
        if (!tasks.isValidTaskNumber(taskNumber)) {
            throw new KaykayException("Please provide an existing task number to delete.");
        }
        int index = Integer.parseInt(taskNumber) - 1;
        Task deletedTask = tasks.remove(index);
        try {
            storage.saveTasks(tasks);
        } catch (IOException exception) {
            tasks.add(index, deletedTask);
            throw exception;
        }
        ui.showDeletedTask(deletedTask, tasks.size());
    }
}
