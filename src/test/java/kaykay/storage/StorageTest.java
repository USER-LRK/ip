package kaykay.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import kaykay.model.ApplicationData;
import kaykay.model.Deadline;
import kaykay.model.Event;
import kaykay.model.Place;
import kaykay.model.PlaceList;
import kaykay.model.TaskList;
import kaykay.model.Todo;

/** Tests task persistence, special-character escaping, and malformed data handling. */
class StorageTest {
    @TempDir
    Path temporaryDirectory;

    /** Checks that all task types and their statuses survive a save/load cycle. */
    @Test
    void saveDataThenLoadData_taskRecords_preservesTaskData() throws IOException {
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

        ApplicationData data = new ApplicationData(tasks);
        storage.saveData(data);
        TaskList loadedTasks = storage.loadData().getTasks();

        assertEquals(3, loadedTasks.size());
        assertEquals(todo.toString(), loadedTasks.getTask(0).toString());
        assertEquals(todo.toFileFormat(), loadedTasks.getTask(0).toFileFormat());
        assertEquals(deadline.toString(), loadedTasks.getTask(1).toString());
        assertEquals(event.toString(), loadedTasks.getTask(2).toString());
    }

    /** Checks missing files and malformed task records. */
    @Test
    void loadData_fileState_returnsExpectedResult() throws IOException {
        Storage missingFileStorage = new Storage(temporaryDirectory.resolve("missing.txt").toString());
        ApplicationData missingData = missingFileStorage.loadData();
        assertEquals(0, missingData.getTasks().size());
        assertEquals(0, missingData.getPlaces().size());

        Path malformedFile = temporaryDirectory.resolve("malformed.txt");
        Files.writeString(malformedFile, "Q | 2 | broken task" + System.lineSeparator());
        Storage malformedStorage = new Storage(malformedFile.toString());
        assertThrows(IOException.class, malformedStorage::loadData);
    }

    /** Checks that task and place records survive a combined save/load cycle. */
    @Test
    void saveDataThenLoadData_mixedRecords_preservesTasksAndPlaces() throws IOException {
        Storage storage = new Storage(temporaryDirectory.resolve("kaykay.txt").toString());
        TaskList tasks = new TaskList(new Todo("visit again"));
        Place place = new Place("Burnt | Ends", "restaurant", "Dempsey",
                LocalDate.of(2026, 9, 10), 5, "Great \\ brisket");
        ApplicationData data = new ApplicationData(tasks, new PlaceList(place));

        storage.saveData(data);
        ApplicationData loadedData = storage.loadData();

        assertEquals(1, loadedData.getTasks().size());
        assertEquals("[T][ ] visit again", loadedData.getTasks().getTask(0).toString());
        assertEquals(1, loadedData.getPlaces().size());
        assertEquals(place.toString(), loadedData.getPlaces().getPlace(0).toString());
        assertEquals(place.toFileFormat(), loadedData.getPlaces().getPlace(0).toFileFormat());
    }

    /** Checks escaping of every character with special meaning in the file format. */
    @Test
    void escape_specialCharacters_returnsStorageSafeText() {
        assertEquals("line\\nreturn\\rpipe\\|slash\\\\",
                Storage.escape("line\nreturn\rpipe|slash\\"));
    }
}
