package kaykay.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import kaykay.model.Deadline;
import kaykay.model.Event;
import kaykay.model.Task;
import kaykay.model.TaskList;
import kaykay.model.Todo;

/** Tests task persistence, special-character escaping, and malformed data handling. */
class StorageTest {
    @TempDir
    Path temporaryDirectory;

    /** Checks that all task types and their statuses survive a save/load cycle. */
    @Test
    void saveTasksThenLoadTasks_roundTrip_preservesTaskData() throws IOException {
        Storage storage = new Storage(temporaryDirectory.resolve("nested/kaykay.txt").toString());
        Todo todo = new Todo("pipe | slash \\");
        todo.mark();
        Deadline deadline = new Deadline("submit report", LocalDateTime.of(2026, 12, 25, 18, 30));
        Event event = new Event("project meeting", LocalDateTime.of(2026, 12, 26, 14, 0),
                LocalDateTime.of(2026, 12, 26, 16, 0));
        TaskList tasks = new TaskList();
        tasks.add(todo);
        tasks.add(deadline);
        tasks.add(event);

        storage.saveTasks(tasks);
        ArrayList<Task> loadedTasks = storage.loadTasks();

        assertEquals(3, loadedTasks.size());
        assertEquals(todo.toString(), loadedTasks.get(0).toString());
        assertEquals(todo.toFileFormat(), loadedTasks.get(0).toFileFormat());
        assertEquals(deadline.toString(), loadedTasks.get(1).toString());
        assertEquals(event.toString(), loadedTasks.get(2).toString());
    }

    /** Checks missing files and malformed task records. */
    @Test
    void loadTasks_fileState_returnsExpectedResult() throws IOException {
        Storage missingFileStorage = new Storage(temporaryDirectory.resolve("missing.txt").toString());
        assertEquals(0, missingFileStorage.loadTasks().size());

        Path malformedFile = temporaryDirectory.resolve("malformed.txt");
        Files.writeString(malformedFile, "Q | 2 | broken task" + System.lineSeparator());
        Storage malformedStorage = new Storage(malformedFile.toString());
        assertThrows(IOException.class, malformedStorage::loadTasks);
    }

    /** Checks escaping of every character with special meaning in the file format. */
    @Test
    void escape_specialCharacters_returnsStorageSafeText() {
        assertEquals("line\\nreturn\\rpipe\\|slash\\\\",
                Storage.escape("line\nreturn\rpipe|slash\\"));
    }
}
