package kaykay.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import kaykay.command.DeadlineCommand;
import kaykay.command.DeleteCommand;
import kaykay.command.EventCommand;
import kaykay.command.ExitCommand;
import kaykay.command.ListCommand;
import kaykay.command.MarkCommand;
import kaykay.command.TodoCommand;
import kaykay.command.UnmarkCommand;
import kaykay.exception.KaykayException;

/** Tests the conversion of user input into executable commands. */
class ParserTest {
    private final Parser parser = new Parser();

    /** Checks that every supported command is mapped to the right command type. */
    @Test
    void parse_supportedCommands_returnsExpectedCommandTypes() throws KaykayException {
        assertInstanceOf(TodoCommand.class, parser.parse("todo revise notes"));
        assertInstanceOf(DeadlineCommand.class,
                parser.parse("deadline submit report /by 27 12 2026 09:00"));
        assertInstanceOf(EventCommand.class,
                parser.parse("event project meeting /from 26 12 2026 14:00 /to 26 12 2026 16:00"));
        assertInstanceOf(DeleteCommand.class, parser.parse("delete 1"));
        assertInstanceOf(MarkCommand.class, parser.parse("mark 1"));
        assertInstanceOf(UnmarkCommand.class, parser.parse("unmark 1"));
        assertInstanceOf(ListCommand.class, parser.parse("list"));
        assertInstanceOf(ExitCommand.class, parser.parse("bye"));
        assertTrue(parser.parse("bye").isExit());
        assertFalse(parser.parse("list").isExit());
    }

    /** Checks the parser's guidance for malformed commands and date/time values. */
    @Test
    void parse_invalidCommands_throwsKaykayExceptionWithGuidance() {
        assertParseError("todo", "A todo needs a description. Try: todo <description>.");
        assertParseError("deadline missing",
                "A deadline needs a description and a date. Try: deadline <description> /by <date>.");
        assertParseError("event meeting /from 31 12 2026 10:00",
                "An event needs a description, start, and end. "
                        + "Try: event <description> /from <start> /to <end>.");
        assertParseError("deadline report /by 31 02 2026 10:00",
                "The deadline date/time '31 02 2026 10:00' is invalid. Please use dd MM yyyy HH:mm, "
                        + "for example 01 01 2026 18:30.");
        assertParseError("blah",
                "I don't recognise that command. Try todo, deadline, event, list, delete, mark, "
                        + "unmark, or bye.");
    }

    /** Verifies one parser failure and its user-facing message. */
    private void assertParseError(String input, String expectedMessage) {
        KaykayException exception = assertThrows(KaykayException.class, () -> parser.parse(input));
        assertEquals(expectedMessage, exception.getMessage());
    }
}
