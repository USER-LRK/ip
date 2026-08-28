package kaykay.model;

import kaykay.storage.Storage;

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

    /**
     * Returns the todo's user-facing display text with its type marker.
     *
     * @return the formatted todo text
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Returns the todo in the format used by the task storage file.
     *
     * @return the serialised todo
     */
    @Override
    public String toFileFormat() {
        return String.format("T | %d | %s", isDone ? 1 : 0, Storage.escape(description));
    }
}
