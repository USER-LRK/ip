package kaykay.model;

import java.time.LocalDateTime;

import kaykay.parser.DateTimeParser;
import kaykay.storage.Storage;

/**
 * A task with a starting date/time and an ending date/time.
 *
 */
public class Event extends Task {
    /** The date and time when this event starts. */
    private final LocalDateTime startDateTime;

    /** The date and time when this event ends. */
    private final LocalDateTime endDateTime;

    /**
     * Creates an event with typed starting and ending date/time values.
     *
     * @param description what the event is about.
     * @param startDateTime when the event starts.
     * @param endDateTime when the event ends.
     */
    public Event(String description, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        super(description);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    /**
     * Checks whether the supplied details identify the same scheduled event.
     *
     * @param description event description to compare.
     * @param startDateTime event start to compare.
     * @param endDateTime event end to compare.
     * @return true if the description and both date/time values match.
     */
    public boolean hasSameDetails(String description, LocalDateTime startDateTime,
            LocalDateTime endDateTime) {
        return getDescription().equals(description)
                && this.startDateTime.equals(startDateTime)
                && this.endDateTime.equals(endDateTime);
    }

    /**
     * Returns the event's user-facing display text with its date/time range.
     *
     * @return the formatted event text.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + DateTimeParser.format(startDateTime)
                + " to: " + DateTimeParser.format(endDateTime) + ")";
    }

    /**
     * Returns the event in the format used by the task storage file.
     *
     * @return the serialized event.
     */
    @Override
    public String toFileFormat() {
        return String.format("E | %d | %s | %s | %s", isDone() ? 1 : 0,
                Storage.escape(getDescription()), Storage.escape(DateTimeParser.format(startDateTime)),
                Storage.escape(DateTimeParser.format(endDateTime)));
    }
}
