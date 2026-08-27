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

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + DateTimeParser.format(by) + ")";
    }

    @Override
    public String toFileFormat() {
        return String.format("D | %d | %s | %s", isDone ? 1 : 0,
                Storage.escape(description), Storage.escape(DateTimeParser.format(by)));
    }
}
