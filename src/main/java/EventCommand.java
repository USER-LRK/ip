import java.time.LocalDateTime;

/**
 * Adds an event task.
 */
public final class EventCommand extends AddCommand {
    /** Event start date and time. */
    private final LocalDateTime from;

    /** Event end date and time. */
    private final LocalDateTime to;

    /** Creates an event command with its description and date/time range. */
    public EventCommand(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    protected Task createTask() {
        return new Event(getDescription(), from, to);
    }
}
