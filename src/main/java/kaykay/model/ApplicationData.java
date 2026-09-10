package kaykay.model;

/**
 * Holds the collections of information managed by Kaykay.
 */
public final class ApplicationData {
    /** Tasks managed by the application. */
    private final TaskList tasks;

    /** Creates application data with an empty task list. */
    public ApplicationData() {
        this(new TaskList());
    }

    /**
     * Creates application data containing the supplied tasks.
     *
     * @param tasks tasks managed by the application.
     */
    public ApplicationData(TaskList tasks) {
        this.tasks = tasks;
    }

    /**
     * Returns the tasks managed by the application.
     *
     * @return the application's task list.
     */
    public TaskList getTasks() {
        return tasks;
    }
}
