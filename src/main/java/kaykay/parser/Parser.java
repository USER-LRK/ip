package kaykay.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import kaykay.command.Command;
import kaykay.command.DeadlineCommand;
import kaykay.command.DeleteCommand;
import kaykay.command.EventCommand;
import kaykay.command.ExitCommand;
import kaykay.command.ListCommand;
import kaykay.command.MarkCommand;
import kaykay.command.TodoCommand;
import kaykay.command.UnmarkCommand;
import kaykay.exception.KaykayException;

/**
 * Interprets user input as executable Kaykay commands.
 */
public final class Parser {
    /** Creates a parser for Kaykay commands. */
    public Parser() {
    }

    /**
     * Parses a complete user input line.
     *
     * @param input user input to interpret.
     * @return an executable command.
     * @throws KaykayException if the input is not a valid Kaykay command.
     */
    public Command parse(String input) throws KaykayException {
        if (input.equals("bye")) {
            return new ExitCommand();
        } else if (input.equals("list")) {
            return new ListCommand();
        } else if (isCommand(input, "delete")) {
            return new DeleteCommand(parseTaskNumber(input,
                    "Please provide an existing task number to delete."));
        } else if (isCommand(input, "mark")) {
            return new MarkCommand(parseTaskNumber(input,
                    "Please provide an existing task number to mark or unmark."));
        } else if (isCommand(input, "unmark")) {
            return new UnmarkCommand(parseTaskNumber(input,
                    "Please provide an existing task number to mark or unmark."));
        } else if (isCommand(input, "todo")) {
            return parseTodo(input);
        } else if (isCommand(input, "deadline")) {
            return parseDeadline(input);
        } else if (isCommand(input, "event")) {
            return parseEvent(input);
        }
        throw new KaykayException("I don't recognise that command. Try todo, deadline, event, "
                + "list, delete, mark, unmark, or bye.");
    }

    /** Parses a todo command and extracts its description. */
    private Command parseTodo(String input) throws KaykayException {
        String description = argumentAfter(input, "todo");
        if (description.trim().isEmpty()) {
            throw new KaykayException("A todo needs a description. Try: todo <description>.");
        }
        return new TodoCommand(description);
    }

    /** Parses a deadline command and converts its date/time. */
    private Command parseDeadline(String input) throws KaykayException {
        String deadlineInput = argumentAfter(input, "deadline");
        String[] deadlineParts = deadlineInput.split(" /by ", 2);
        if (deadlineParts.length != 2 || deadlineParts[0].trim().isEmpty()
                || deadlineParts[1].trim().isEmpty()) {
            throw new KaykayException("A deadline needs a description and a date. "
                    + "Try: deadline <description> /by <date>.");
        }

        String byText = deadlineParts[1].trim();
        try {
            LocalDateTime by = DateTimeParser.parse(byText);
            return new DeadlineCommand(deadlineParts[0], by);
        } catch (DateTimeParseException exception) {
            throw invalidDateTime("deadline", byText);
        }
    }

    /** Parses an event command and converts its start and end date/times. */
    private Command parseEvent(String input) throws KaykayException {
        String eventInput = argumentAfter(input, "event");
        String[] fromParts = eventInput.split(" /from ", 2);
        if (fromParts.length != 2 || fromParts[0].trim().isEmpty()) {
            throw invalidEventFormat();
        }

        String[] toParts = fromParts[1].split(" /to ", 2);
        if (toParts.length != 2 || toParts[0].trim().isEmpty() || toParts[1].trim().isEmpty()) {
            throw invalidEventFormat();
        }

        String fromText = toParts[0].trim();
        String toText = toParts[1].trim();
        LocalDateTime from;
        LocalDateTime to;
        try {
            from = DateTimeParser.parse(fromText);
        } catch (DateTimeParseException exception) {
            throw invalidDateTime("event start", fromText);
        }
        try {
            to = DateTimeParser.parse(toText);
        } catch (DateTimeParseException exception) {
            throw invalidDateTime("event end", toText);
        }
        return new EventCommand(fromParts[0], from, to);
    }

    /** Parses the numeric argument shared by delete, mark, and unmark. */
    private String parseTaskNumber(String input, String errorMessage) throws KaykayException {
        String[] pieces = input.split("\\s+");
        if (pieces.length != 2 || !isInteger(pieces[1])) {
            throw new KaykayException(errorMessage);
        }
        return pieces[1];
    }

    /** Returns the text after a command name while preserving existing spacing behavior. */
    private static String argumentAfter(String input, String command) {
        return input.length() == command.length() ? "" : input.substring(command.length() + 1);
    }

    /** Checks whether a value can be interpreted as an integer task number. */
    private static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    /** Checks whether an input is a command or starts with that command and an argument. */
    private static boolean isCommand(String input, String command) {
        return input.equals(command) || input.startsWith(command + " ");
    }

    /** Builds the standard invalid-event-format error. */
    private static KaykayException invalidEventFormat() {
        return new KaykayException("An event needs a description, start, and end. "
                + "Try: event <description> /from <start> /to <end>.");
    }

    /** Builds a date/time error that identifies the invalid input and expected format. */
    private static KaykayException invalidDateTime(String field, String value) {
        return new KaykayException(String.format(
                "The %s date/time '%s' is invalid. Please use %s, for example %s.",
                field, value, DateTimeParser.DATE_TIME_INPUT_FORMAT,
                DateTimeParser.DATE_TIME_EXAMPLE));
    }
}
