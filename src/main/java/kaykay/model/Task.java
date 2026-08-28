package kaykay.model;

import kaykay.storage.Storage;

/**
 * Represents a task that can be marked as done or not done.
 *
 * // AI-GENERATED: This class documentation was added as part of the Level 4 implementation.
 */
public class Task {
    /** The text describing what needs to be done. */
    protected String description;

    /** Whether this task has been completed. */
    protected boolean isDone;

    /**
     * Creates an unfinished task with the given description.
     *
     * @param description what needs to be done
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the completion marker used in the task's display text.
     *
     * @return {@code "X"} when the task is done, otherwise a blank space
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /** Marks this task as completed. */
    public void mark() {
        this.isDone = true;
    }

    /** Marks this task as not completed. */
    public void unmark() {
        this.isDone = false;
    }

    /**
     * Returns the task's user-facing display text.
     *
     * @return the task type-independent display text
     */
    @Override
    public String toString() {
        return String.format("[%s] %s", this.getStatusIcon(), this.description);
    }

    /**
     * Returns the task in the format used by the task storage file.
     *
     * @return the serialised task
     */
    public String toFileFormat() {
        return String.format("T | %d | %s", isDone ? 1 : 0, Storage.escape(description));
    }
}
