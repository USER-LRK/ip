import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Interprets user input as validated Kaykay commands.
 */
public final class Parser {
    /** The command kinds understood by Kaykay. */
    public enum CommandType {
        BYE,
        LIST,
        DELETE,
        MARK,
        UNMARK,
        TODO,
        DEADLINE,
        EVENT
    }

    /**
     * Immutable result of parsing one user command.
     */
    public static final class Command {
        /** The kind of command that was parsed. */
        private final CommandType type;

        /** Text associated with the command, such as a description or task number. */
        private final String value;

        /** First date/time associated with the command, if any. */
        private final LocalDateTime start;

        /** Second date/time associated with the command, if any. */
        private final LocalDateTime end;

        private Command(CommandType type, String value, LocalDateTime start, LocalDateTime end) {
            this.type = type;
            this.value = value;
            this.start = start;
            this.end = end;
        }

        /** @return the parsed command kind */
        public CommandType getType() {
            return type;
        }

        /** @return the command's text value, if one was supplied */
        public String getValue() {
            return value;
        }

        /** @return the command's first date/time, if one was supplied */
        public LocalDateTime getStart() {
            return start;
        }

        /** @return the command's second date/time, if one was supplied */
        public LocalDateTime getEnd() {
            return end;
        }
    }

    /**
     * Parses a complete user input line.
     *
     * @param input user input to interpret
     * @return the parsed command
     * @throws KaykayException if the input is not a valid Kaykay command
     */
    public Command parse(String input) throws KaykayException {
        if (input.equals("bye")) {
            return new Command(CommandType.BYE, null, null, null);
        } else if (input.equals("list")) {
            return new Command(CommandType.LIST, null, null, null);
        } else if (isCommand(input, "delete")) {
            return parseTaskNumber(input, CommandType.DELETE,
                    "Please provide an existing task number to delete.");
        } else if (isCommand(input, "mark")) {
            return parseTaskNumber(input, CommandType.MARK,
                    "Please provide an existing task number to mark or unmark.");
        } else if (isCommand(input, "unmark")) {
            return parseTaskNumber(input, CommandType.UNMARK,
                    "Please provide an existing task number to mark or unmark.");
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
        return new Command(CommandType.TODO, description, null, null);
    }

    /** Parses a deadline command and converts its date/time text. */
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
            return new Command(CommandType.DEADLINE, deadlineParts[0],
                    DateTimeParser.parse(byText), null);
        } catch (DateTimeParseException exception) {
            throw invalidDateTime("deadline", byText);
        }
    }

    /** Parses an event command and converts its start and end date/time text. */
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
        return new Command(CommandType.EVENT, fromParts[0], from, to);
    }

    /** Parses the numeric argument shared by delete, mark, and unmark. */
    private Command parseTaskNumber(String input, CommandType type, String errorMessage)
            throws KaykayException {
        String[] pieces = input.split("\\s+");
        if (pieces.length != 2 || !isInteger(pieces[1])) {
            throw new KaykayException(errorMessage);
        }
        return new Command(type, pieces[1], null, null);
    }

    /** Returns the text after a command name while preserving the existing spacing behavior. */
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
                field, value, DateTimeParser.INPUT_FORMAT, DateTimeParser.EXAMPLE));
    }
}
