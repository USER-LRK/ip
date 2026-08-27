package kaykay.command;

import java.io.IOException;

import kaykay.exception.KaykayException;
import kaykay.model.Task;
import kaykay.model.TaskList;
import kaykay.storage.Storage;
import kaykay.ui.Ui;

/**
 * Provides shared persistence and rollback behavior for mark commands.
 */
public abstract class ChangeStatusCommand extends Command {
    /** User-supplied one-based task number. */
    private final String taskNumber;

    /** Whether the command marks the task done rather than not done. */
    private final boolean marked;

    /** Creates a status-change command. */
    protected ChangeStatusCommand(String taskNumber, boolean marked) {
        this.taskNumber = taskNumber;
        this.marked = marked;
    }

    @Override
    public final void execute(TaskList tasks, Ui ui, Storage storage)
            throws KaykayException, IOException {
        if (!tasks.isValidTaskNumber(taskNumber)) {
            throw new KaykayException("Please provide an existing task number to mark or unmark.");
        }
        int index = Integer.parseInt(taskNumber) - 1;
        Task changedTask = tasks.getTask(index);
        boolean wasDone = changedTask.getStatusIcon().equals("X");
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
    }
}
