package kaykay.model;

import java.time.LocalDateTime;

import kaykay.parser.DateTimeParser;
import kaykay.storage.Storage;

/**
 * A task that must be completed by a specified date or time.
 *
 * // AI-GENERATED: This class was added as part of the Level 4 implementation.
 */
public class Deadline extends Task {
    /** The date and time by which this task should be completed. */
    protected LocalDateTime by;

    /**
     * Creates a deadline with a typed date/time value.
     *
     * @param description what needs to be done
     * @param by the deadline date and time
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the deadline's user-facing display text with its due date/time.
     *
     * @return the formatted deadline text
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + DateTimeParser.format(by) + ")";
    }

    /**
     * Returns the deadline in the format used by the task storage file.
     *
     * @return the serialised deadline
     */
    @Override
    public String toFileFormat() {
        return String.format("D | %d | %s | %s", isDone ? 1 : 0,
                Storage.escape(description), Storage.escape(DateTimeParser.format(by)));
    }
}
