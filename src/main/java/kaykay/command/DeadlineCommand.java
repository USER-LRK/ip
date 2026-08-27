package kaykay.command;

import java.time.LocalDateTime;

import kaykay.model.Deadline;
import kaykay.model.Task;

/**
 * Adds a deadline task.
 */
public final class DeadlineCommand extends AddCommand {
    /** Date and time by which the task should be completed. */
    private final LocalDateTime by;

    /** Creates a deadline command with its description and due date/time. */
    public DeadlineCommand(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    @Override
    protected Task createTask() {
        return new Deadline(getDescription(), by);
    }
}
