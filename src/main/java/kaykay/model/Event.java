package kaykay.model;

import java.time.LocalDateTime;

import kaykay.parser.DateTimeParser;
import kaykay.storage.Storage;

/**
 * A task with a starting date/time and an ending date/time.
 *
 * // AI-GENERATED: This class was added as part of the Level 4 implementation.
 */
public class Event extends Task {
    /** The date and time when this event starts. */
    protected LocalDateTime from;

    /** The date and time when this event ends. */
    protected LocalDateTime to;

    /**
     * Creates an event with typed starting and ending date/time values.
     *
     * @param description what the event is about
     * @param from when the event starts
     * @param to when the event ends
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event's user-facing display text with its date/time range.
     *
     * @return the formatted event text
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + DateTimeParser.format(from)
                + " to: " + DateTimeParser.format(to) + ")";
    }

    /**
     * Returns the event in the format used by the task storage file.
     *
     * @return the serialised event
     */
    @Override
    public String toFileFormat() {
        return String.format("E | %d | %s | %s | %s", isDone ? 1 : 0,
                Storage.escape(description), Storage.escape(DateTimeParser.format(from)),
                Storage.escape(DateTimeParser.format(to)));
    }
}
