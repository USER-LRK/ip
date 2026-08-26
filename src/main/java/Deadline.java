/**
 * A task that must be completed by a specified date or time.
 *
 * // AI-GENERATED: This class was added as part of the Level 4 implementation.
 */
public class Deadline extends Task {
    protected String by;

    /**
     * Creates a deadline without interpreting the date/time string.
     *
     * @param description what needs to be done
     * @param by the deadline, kept as entered by the user
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    @Override
    public String toFileFormat() {
        return String.format("D | %d | %s | %s", isDone ? 1 : 0,
                Storage.escape(description), Storage.escape(by));
    }
}
