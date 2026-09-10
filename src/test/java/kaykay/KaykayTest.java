package kaykay;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import kaykay.storage.Storage;
import kaykay.ui.Ui;

/** Tests one-command processing independently of the console input loop. */
class KaykayTest {
    @TempDir
    Path temporaryDirectory;

    /** Checks that commands execute and render their responses through the UI. */
    @Test
    void processCommand_validCommand_updatesTasksAndRendersResponse() {
        StringBuilder output = new StringBuilder();
        Ui ui = new Ui(line -> output.append(line).append(System.lineSeparator()));
        Kaykay kaykay = new Kaykay(temporaryDirectory.resolve("tasks.txt").toString(), ui);

        assertFalse(kaykay.processCommand("todo buy milk"));
        assertTrue(output.toString().contains("I've added this task:"));
        assertTrue(output.toString().contains("[T][ ] buy milk"));
        assertTrue(kaykay.processCommand("bye"));
    }

    /** Checks that GUI output omits separators while retaining response text. */
    @Test
    void processCommand_guiOutput_omitsConsoleSeparators() {
        StringBuilder output = new StringBuilder();
        Ui ui = new Ui(line -> output.append(line).append(System.lineSeparator()), false);
        Kaykay kaykay = new Kaykay(temporaryDirectory.resolve("gui-tasks.txt").toString(), ui);

        kaykay.processCommand("todo buy milk");

        assertFalse(output.toString().contains("____________________________________________________________"));
        assertTrue(output.toString().contains("I've added this task:"));
    }

    /** Checks that a blank command produces concise and predictable guidance. */
    @Test
    void processCommand_blankCommand_showsInputWarning() {
        StringBuilder output = new StringBuilder();
        Ui ui = new Ui(line -> output.append(line).append(System.lineSeparator()), false);
        Kaykay kaykay = new Kaykay(temporaryDirectory.resolve("blank-command.txt").toString(), ui);

        assertFalse(kaykay.processCommand("   "));

        assertEquals("OOPS! Please enter a command." + System.lineSeparator(), output.toString());
    }

    /** Checks that place commands work together through the application coordinator. */
    @Test
    void processCommand_placeWorkflow_managesPersistedPlaces() throws IOException {
        StringBuilder output = new StringBuilder();
        Ui ui = new Ui(line -> output.append(line).append(System.lineSeparator()), false);
        Path dataFile = temporaryDirectory.resolve("place-data.txt");
        Kaykay kaykay = new Kaykay(dataFile.toString(), ui);

        kaykay.processCommand("place add Burnt Ends /type restaurant /location Dempsey "
                + "/visited 10 09 2026 /rating 5 /notes Great brisket");
        kaykay.processCommand("place edit 1 /rating 4 /notes Worth revisiting");
        kaykay.processCommand("place find REVISITING");
        kaykay.processCommand("place list");

        assertTrue(output.toString().contains("I've saved this place:"));
        assertTrue(output.toString().contains("rating: 4/5; notes: Worth revisiting"));
        assertTrue(output.toString().contains("Here are the matching places:"));
        assertEquals(1, new Storage(dataFile.toString()).loadData().getPlaces().size());
    }
}
