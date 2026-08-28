package kaykay.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import kaykay.exception.KaykayException;
import kaykay.model.Task;
import kaykay.model.TaskList;
import kaykay.storage.Storage;
import kaykay.ui.Ui;

/** Tests command behavior that mutates task state and persists the result. */
class CommandTest {
    @TempDir
    Path temporaryDirectory;

    /** Checks that adding a task updates both memory and storage. */
    @Test
    void todoCommand_execute_addsAndSavesTask() throws IOException, KaykayException {
        TaskList tasks = new TaskList();
        Storage storage = new Storage(temporaryDirectory.resolve("tasks.txt").toString());

        new TodoCommand("buy milk").execute(tasks, new RecordingUi(), storage);

        assertEquals(1, tasks.size());
        assertEquals("[T][ ] buy milk", tasks.getTask(0).toString());
        assertEquals("T | 0 | buy milk", storage.loadTasks().get(0).toFileFormat());
    }

    /** Checks marking, unmarking, deletion, and rejection of an invalid task number. */
    @Test
    void statusAndDeleteCommands_execute_updateTaskList() throws IOException, KaykayException {
        TaskList tasks = new TaskList();
        Storage storage = new Storage(temporaryDirectory.resolve("tasks.txt").toString());
        new TodoCommand("revise notes").execute(tasks, new RecordingUi(), storage);

        new MarkCommand("1").execute(tasks, new RecordingUi(), storage);
        assertEquals("[T][X] revise notes", tasks.getTask(0).toString());
        new UnmarkCommand("1").execute(tasks, new RecordingUi(), storage);
        assertEquals("[T][ ] revise notes", tasks.getTask(0).toString());
        new DeleteCommand("1").execute(tasks, new RecordingUi(), storage);
        assertEquals(0, tasks.size());
        assertThrows(KaykayException.class,
                () -> new DeleteCommand("1").execute(tasks, new RecordingUi(), storage));
    }

    /** Suppresses UI output while allowing commands to execute in isolation. */
    private static final class RecordingUi extends Ui {
        @Override
        public void showAddedTask(Task task, int taskCount) {
            // Intentionally empty: command tests assert state, not console formatting.
        }

        @Override
        public void showDeletedTask(Task task, int taskCount) {
            // Intentionally empty: command tests assert state, not console formatting.
        }

        @Override
        public void showMarkedTask(Task task, boolean marked) {
            // Intentionally empty: command tests assert state, not console formatting.
        }
    }
}
