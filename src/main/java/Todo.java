/**
 * A task without an associated date or time.
 *
 * // AI-GENERATED: This class was added as part of the Level 4 implementation.
 */
public class Todo extends Task {
    /**
     * Creates a todo task.
     *
     * @param description what needs to be done
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String toFileFormat() {
        return String.format("T | %d | %s", isDone ? 1 : 0, description);
    }
}
