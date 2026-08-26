/**
 * Represents a task that can be marked as done or not done.
 *
 * // AI-GENERATED: This class documentation was added as part of the Level 4 implementation.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

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
