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

    /**
     * Creates a deadline command with its description and due date/time.
     *
     * @param description description of the deadline task.
     * @param by date and time by which the task should be completed.
     */
    public DeadlineCommand(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Creates the deadline represented by this command.
     *
     * @return a deadline with the command's description and due date/time.
     */
    @Override
    protected Task createTask() {
        return new Deadline(getDescription(), by);
    }
}
