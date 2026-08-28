package kaykay.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import kaykay.exception.KaykayException;
import kaykay.model.Task;
import kaykay.model.TaskList;
import kaykay.model.Todo;
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

    /** Checks that find delegates the ordered matches to the UI without changing task state. */
    @Test
    void findCommand_execute_displaysMatchingTasks() throws IOException, KaykayException {
        Task first = new Todo("read book");
        Task second = new Todo("buy milk");
        TaskList tasks = new TaskList(List.of(first, second));
        RecordingUi ui = new RecordingUi();

        new FindCommand("BOOK").execute(tasks, ui,
                new Storage(temporaryDirectory.resolve("tasks.txt").toString()));

        assertEquals(List.of(first), ui.getMatchingTasks());
        assertEquals(2, tasks.size());
    }

    /** Suppresses UI output while allowing commands to execute in isolation. */
    private static final class RecordingUi extends Ui {
        /** Most recent search results shown by this UI. */
        private List<Task> matchingTasks;

        /** Does nothing because these tests verify command state changes directly. */
        @Override
        public void showAddedTask(Task task, int taskCount) {
            // Intentionally empty: command tests assert state, not console formatting.
        }

        /** Does nothing because these tests verify command state changes directly. */
        @Override
        public void showDeletedTask(Task task, int taskCount) {
            // Intentionally empty: command tests assert state, not console formatting.
        }

        /** Does nothing because these tests verify command state changes directly. */
        @Override
        public void showMarkedTask(Task task, boolean marked) {
            // Intentionally empty: command tests assert state, not console formatting.
        }

        /** Records search results for command assertions. */
        @Override
        public void showMatchingTasks(List<Task> matchingTasks) {
            this.matchingTasks = matchingTasks;
        }

        /** Returns the search results recorded by this UI. */
        private List<Task> getMatchingTasks() {
            return matchingTasks;
        }
    }
}
