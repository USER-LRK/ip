/**
 * Adds a todo task.
 */
public final class TodoCommand extends AddCommand {
    /** Creates a todo command with its description. */
    public TodoCommand(String description) {
        super(description);
    }

    @Override
    protected Task createTask() {
        return new Todo(getDescription());
    }
}
