import java.io.IOException;

/**
 * Ends the current Kaykay session.
 */
public final class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws IOException {
        // The run loop checks isExit() after execution.
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
