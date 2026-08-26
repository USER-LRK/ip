/**
 * A task with a starting date/time and an ending date/time.
 *
 * // AI-GENERATED: This class was added as part of the Level 4 implementation.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates an event without interpreting either date/time string.
     *
     * @param description what the event is about
     * @param from when the event starts, kept as entered by the user
     * @param to when the event ends, kept as entered by the user
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }

    @Override
    public String toFileFormat() {
        return String.format("E | %d | %s | %s | %s", isDone ? 1 : 0, description, from, to);
    }
}
