/**
 * Marks a task as not done.
 */
public final class UnmarkCommand extends ChangeStatusCommand {
    /** Creates an unmark command for a task number. */
    public UnmarkCommand(String taskNumber) {
        super(taskNumber, false);
    }
}
