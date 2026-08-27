package kaykay.command;

import java.io.IOException;

import kaykay.model.TaskList;
import kaykay.storage.Storage;
import kaykay.ui.Ui;

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
