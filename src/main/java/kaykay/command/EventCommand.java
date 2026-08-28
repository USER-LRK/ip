package kaykay.command;

import java.time.LocalDateTime;

import kaykay.model.Event;
import kaykay.model.Task;

/**
 * Adds an event task.
 */
public final class EventCommand extends AddCommand {
    /** Event start date and time. */
    private final LocalDateTime from;

    /** Event end date and time. */
    private final LocalDateTime to;

    /**
     * Creates an event command with its description and date/time range.
     *
     * @param description description of the event.
     * @param from date and time when the event starts.
     * @param to date and time when the event ends.
     */
    public EventCommand(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Creates the event represented by this command.
     *
     * @return an event with the command's description and date/time range.
     */
    @Override
    protected Task createTask() {
        return new Event(getDescription(), from, to);
    }
}
