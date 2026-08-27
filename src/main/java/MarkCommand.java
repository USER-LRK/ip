/**
 * Marks a task as done.
 */
public final class MarkCommand extends ChangeStatusCommand {
    /** Creates a mark command for a task number. */
    public MarkCommand(String taskNumber) {
        super(taskNumber, true);
    }
}
