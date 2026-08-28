package kaykay.command;

import java.io.IOException;

import kaykay.model.TaskList;
import kaykay.storage.Storage;
import kaykay.ui.Ui;

/**
 * Ends the current Kaykay session.
 */
public final class ExitCommand extends Command {
    /** Creates an exit command. */
    public ExitCommand() {
    }

    /**
     * Performs no task action because the caller checks {@link #isExit()} after execution.
     *
     * @param tasks task list managed by the chatbot.
     * @param ui UI managed by the chatbot.
     * @param storage storage managed by the chatbot.
     * @throws IOException declared by the command interface; not thrown by exiting.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws IOException {
        // The run loop checks isExit() after execution.
    }

    /**
     * Indicates that this command ends the chatbot session.
     *
     * @return always {@code true}.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
