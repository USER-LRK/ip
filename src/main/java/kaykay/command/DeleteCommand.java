package kaykay.command;

import java.io.IOException;

import kaykay.exception.KaykayException;
import kaykay.model.Task;
import kaykay.model.TaskList;
import kaykay.storage.Storage;
import kaykay.ui.Ui;

/**
 * Deletes a task and persists the resulting task list.
 */
public final class DeleteCommand extends Command {
    /** User-supplied one-based task number. */
    private final String taskNumber;

    /**
     * Creates a delete command for a task number.
     *
     * @param taskNumber one-based number of the task to delete.
     */
    public DeleteCommand(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * Deletes the selected task, saves the result, and confirms the deletion.
     * The task is restored if saving fails.
     *
     * @param tasks task list to modify.
     * @param ui UI used to show the confirmation.
     * @param storage storage used to persist the updated task list.
     * @throws KaykayException if the task number is invalid.
     * @throws IOException if the updated task list cannot be saved.
     */
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
