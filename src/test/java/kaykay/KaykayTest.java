package kaykay;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

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
}
