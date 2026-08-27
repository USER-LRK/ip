package kaykay.command;

import java.io.IOException;

import kaykay.model.TaskList;
import kaykay.storage.Storage;
import kaykay.ui.Ui;

/**
 * Displays all tasks in their current order.
 */
public final class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws IOException {
        ui.showTaskList(tasks);
    }
}
