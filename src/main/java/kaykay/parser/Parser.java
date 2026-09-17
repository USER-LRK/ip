package kaykay.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kaykay.command.AddPlaceCommand;
import kaykay.command.Command;
import kaykay.command.DeadlineCommand;
import kaykay.command.DeleteCommand;
import kaykay.command.DeletePlaceCommand;
import kaykay.command.EditPlaceCommand;
import kaykay.command.EventCommand;
import kaykay.command.ExitCommand;
import kaykay.command.FindCommand;
import kaykay.command.FindPlacesCommand;
import kaykay.command.ListCommand;
import kaykay.command.ListPlacesCommand;
import kaykay.command.MarkCommand;
import kaykay.command.TodoCommand;
import kaykay.command.UnmarkCommand;
import kaykay.exception.KaykayException;
import kaykay.model.Place;

/**
 * Interprets user input as executable Kaykay commands.
 */
public final class Parser {
    /** Recognizes slash-prefixed fields in place details. */
    private static final Pattern PLACE_FIELD_PATTERN = Pattern.compile(" /([a-z]+)(?: |$)");

    /** Fields accepted when adding a place. */
    private static final Set<String> PLACE_ADD_FIELDS = Set.of(
            "type", "location", "visited", "rating", "notes");

    /** Fields accepted when editing a place. */
    private static final Set<String> PLACE_EDIT_FIELDS = Set.of(
            "name", "type", "location", "visited", "rating", "notes");

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
        if (input.isBlank()) {
            throw new KaykayException("Please enter a command.");
        } else if (input.equals("bye")) {
            return parseExit();
        } else if (input.equals("list")) {
            return parseList();
        } else if (isCommand(input, "find")) {
            return parseFind(input);
        } else if (isCommand(input, "place")) {
            return parsePlaceCommand(input);
        } else if (isCommand(input, "delete")) {
            return parseDelete(input);
        } else if (isCommand(input, "mark")) {
            return parseMark(input);
        } else if (isCommand(input, "unmark")) {
            return parseUnmark(input);
        } else if (isCommand(input, "todo")) {
            return parseTodo(input);
        } else if (isCommand(input, "deadline")) {
            return parseDeadline(input);
        } else if (isCommand(input, "event")) {
            return parseEvent(input);
        }
        throw unknownCommand();
    }

    /** Parses one command in the place-management command family. */
    private Command parsePlaceCommand(String input) throws KaykayException {
        if (input.equals("place list")) {
            return new ListPlacesCommand();
        } else if (isCommand(input, "place add")) {
            return parseAddPlace(input);
        } else if (isCommand(input, "place find")) {
            return parseFindPlaces(input);
        } else if (isCommand(input, "place edit")) {
            return parseEditPlace(input);
        } else if (isCommand(input, "place delete")) {
            return new DeletePlaceCommand(parsePlaceNumber(input, "place delete", "delete"));
        }
        throw new KaykayException("I don't recognise that place command. "
                + "Try place add, place list, place find, place edit, or place delete.");
    }

    /** Parses a place-add command and its optional details. */
    private Command parseAddPlace(String input) throws KaykayException {
        PlaceFields fields = parsePlaceFields(argumentAfter(input, "place add"), true,
                PLACE_ADD_FIELDS);
        return new AddPlaceCommand(createPlace(fields));
    }

    /** Parses a place search and extracts its keyword. */
    private Command parseFindPlaces(String input) throws KaykayException {
        String keyword = argumentAfter(input, "place find").trim();
        if (keyword.isEmpty()) {
            throw new KaykayException("A place find command needs a keyword. "
                    + "Try: place find <keyword>.");
        }
        return new FindPlacesCommand(keyword);
    }

    /** Parses a partial update to an existing place. */
    private Command parseEditPlace(String input) throws KaykayException {
        String editInput = argumentAfter(input, "place edit").trim();
        int firstSpace = editInput.indexOf(' ');
        if (firstSpace < 0 || !isInteger(editInput.substring(0, firstSpace))) {
            throw invalidPlaceEdit();
        }

        String placeNumber = editInput.substring(0, firstSpace);
        String fieldInput = editInput.substring(firstSpace);
        PlaceFields fields = parsePlaceFields(fieldInput, false, PLACE_EDIT_FIELDS);
        if (!fields.name.isEmpty() || fields.values.isEmpty()) {
            throw invalidPlaceEdit();
        }
        return new EditPlaceCommand(placeNumber, fields.get("name"), fields.get("type"),
                fields.get("location"), parseVisitDate(fields.get("visited")),
                parseRating(fields.get("rating")), fields.get("notes"));
    }

    /** Creates a place from parsed and validated field text. */
    private Place createPlace(PlaceFields fields) throws KaykayException {
        return new Place(fields.name, fields.get("type"), fields.get("location"),
                parseVisitDate(fields.get("visited")), parseRating(fields.get("rating")),
                fields.get("notes"));
    }

    /** Parses the name and slash-prefixed details from a place command. */
    private PlaceFields parsePlaceFields(String input, boolean isNameRequired, Set<String> allowedFields)
            throws KaykayException {
        Map<String, String> values = new HashMap<>();
        Matcher matcher = PLACE_FIELD_PATTERN.matcher(input);
        int firstFieldStart = -1;
        String previousField = null;
        int previousValueStart = -1;
        while (matcher.find()) {
            if (previousField == null) {
                firstFieldStart = matcher.start();
            } else {
                addPlaceField(values, previousField,
                        input.substring(previousValueStart, matcher.start()), allowedFields);
            }
            previousField = matcher.group(1);
            previousValueStart = matcher.end();
        }
        if (previousField != null) {
            addPlaceField(values, previousField, input.substring(previousValueStart), allowedFields);
        }

        String name = (firstFieldStart < 0 ? input : input.substring(0, firstFieldStart)).trim();
        if (isNameRequired && name.isEmpty()) {
            throw new KaykayException("A place needs a name. Try: place add <name> "
                    + "[/type <type>] [/location <location>] [/visited <date>] "
                    + "[/rating <1-5>] [/notes <notes>].");
        }
        return new PlaceFields(name, values);
    }

    /** Validates and records one parsed place field. */
    private void addPlaceField(Map<String, String> values, String field, String value,
            Set<String> allowedFields) throws KaykayException {
        if (!allowedFields.contains(field)) {
            throw new KaykayException("Unknown place field '/" + field + "'.");
        }
        String trimmedValue = value.trim();
        if (trimmedValue.isEmpty()) {
            throw new KaykayException("The /" + field + " field needs a value.");
        }
        if (values.putIfAbsent(field, trimmedValue) != null) {
            throw new KaykayException("The /" + field + " field can only be used once.");
        }
    }

    /** Parses an optional visit date. */
    private LocalDate parseVisitDate(String value) throws KaykayException {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value, Place.VISIT_DATE_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new KaykayException("The place visit date '" + value + "' is invalid. "
                    + "Please use dd MM yyyy, for example 10 09 2026.");
        }
    }

    /** Parses and validates an optional rating. */
    private Integer parseRating(String value) throws KaykayException {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            int rating = Integer.parseInt(value);
            if (rating >= 1 && rating <= 5) {
                return rating;
            }
        } catch (NumberFormatException exception) {
            // The shared message below explains both non-numeric and out-of-range ratings.
        }
        throw new KaykayException("A place rating must be a whole number from 1 to 5.");
    }

    /** Parses a one-based place number for a place command. */
    private String parsePlaceNumber(String input, String command, String action) throws KaykayException {
        String value = argumentAfter(input, command).trim();
        if (value.isEmpty() || value.contains(" ") || !isInteger(value)) {
            throw new KaykayException("Please provide an existing place number to " + action + ".");
        }
        return value;
    }

    /** Builds the standard malformed-place-edit error. */
    private static KaykayException invalidPlaceEdit() {
        return new KaykayException("A place edit needs a place number and at least one field. "
                + "Try: place edit <number> /rating <1-5>.");
    }

    /** Builds the standard unknown-command error. */
    private static KaykayException unknownCommand() {
        return new KaykayException("I don't recognise that command. Try todo, deadline, event, "
                + "list, find, delete, mark, unmark, place, or bye.");
    }

    /** Parses an exit command. */
    private Command parseExit() {
        return new ExitCommand();
    }

    /** Parses a list command. */
    private Command parseList() {
        return new ListCommand();
    }

    /** Parses a find command and extracts its search keyword. */
    private Command parseFind(String input) throws KaykayException {
        String keyword = argumentAfter(input, "find").trim();
        if (keyword.isEmpty()) {
            throw new KaykayException("A find command needs a keyword. Try: find <keyword>.");
        }
        return new FindCommand(keyword);
    }

    /** Parses a delete command and extracts its task number. */
    private Command parseDelete(String input) throws KaykayException {
        String taskNumber = parseTaskNumber(input,
                "Please provide an existing task number to delete.");
        return new DeleteCommand(taskNumber);
    }

    /** Parses a mark command and extracts its task number. */
    private Command parseMark(String input) throws KaykayException {
        String taskNumber = parseTaskNumber(input,
                "Please provide an existing task number to mark or unmark.");
        return new MarkCommand(taskNumber);
    }

    /** Parses an unmark command and extracts its task number. */
    private Command parseUnmark(String input) throws KaykayException {
        String taskNumber = parseTaskNumber(input,
                "Please provide an existing task number to mark or unmark.");
        return new UnmarkCommand(taskNumber);
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
            throw new KaykayException("A deadline needs a description, date, and time. "
                    + "Use: deadline <description> /by dd MM yyyy HH:mm.");
        }

        String byText = deadlineParts[1].trim();
        LocalDateTime deadlineDateTime = parseDateTime("deadline", byText);
        return new DeadlineCommand(deadlineParts[0], deadlineDateTime);
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
        LocalDateTime startDateTime = parseDateTime("event start", fromText);
        LocalDateTime endDateTime = parseDateTime("event end", toText);
        if (endDateTime.isBefore(startDateTime)) {
            throw new KaykayException("The event end cannot be before its start.");
        }
        return new EventCommand(fromParts[0], startDateTime, endDateTime);
    }

    /** Parses a date/time and reports invalid input in the command's context. */
    private static LocalDateTime parseDateTime(String field, String value) throws KaykayException {
        try {
            return DateTimeParser.parse(value);
        } catch (DateTimeParseException exception) {
            throw invalidDateTime(field, value);
        }
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
        assert isCommand(input, command) : "Input must match the command before extracting its argument";
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
        return new KaykayException("An event needs a description plus start and end date/times. "
                + "Use: event <description> /from dd MM yyyy HH:mm /to dd MM yyyy HH:mm.");
    }

    /** Builds a date/time error that identifies the invalid input and expected format. */
    private static KaykayException invalidDateTime(String field, String value) {
        return new KaykayException(String.format(
                "The %s date/time '%s' is invalid. Include both the date and 24-hour time: %s "
                        + "(spaces between day, month, and year), e.g. %s.",
                field, value, DateTimeParser.DATE_TIME_INPUT_FORMAT,
                DateTimeParser.DATE_TIME_EXAMPLE));
    }

    /** Holds the name and optional details parsed from a place command. */
    private static final class PlaceFields {
        /** Bare place name before the first slash-prefixed field. */
        private final String name;

        /** Parsed field values keyed by their command names. */
        private final Map<String, String> values;

        /** Creates parsed place fields. */
        private PlaceFields(String name, Map<String, String> values) {
            this.name = name;
            this.values = values;
        }

        /** Returns a field value, using an empty string for an omitted add field. */
        private String get(String field) {
            if (!name.isEmpty()) {
                return values.getOrDefault(field, "");
            }
            return values.get(field);
        }
    }
}
