package kaykay.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * Parses and formats task date/time values using Kaykay's standard format.
 */
public final class DateTimeParser {
    /** The date/time format accepted from users and written to task output. */
    public static final String INPUT_FORMAT = "dd MM yyyy HH:mm";

    /** An example date/time shown in input guidance. */
    public static final String EXAMPLE = "01 01 2026 18:30";

    /**
     * The format shown to users: day, month, year, 24-hour time.
     *
     * The formatter uses {@code uuuu} internally so that strict parsing handles
     * the year correctly without requiring an era.
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .parseCaseSensitive()
            .appendPattern("dd MM uuuu HH:mm")
            .toFormatter(Locale.ROOT)
            .withResolverStyle(ResolverStyle.STRICT);

    private DateTimeParser() {
        // Prevent instantiation of this utility class.
    }

    /**
     * Parses a date/time in the format {@code dd MM yyyy HH:mm}.
     *
     * @param value date/time text to parse
     * @return the parsed date/time
     * @throws java.time.format.DateTimeParseException if the value is invalid
     */
    public static LocalDateTime parse(String value) {
        return LocalDateTime.parse(value, DATE_TIME_FORMATTER);
    }

    /**
     * Formats a date/time in the format {@code dd MM yyyy HH:mm}.
     *
     * @param value date/time to format
     * @return formatted date/time text
     */
    public static String format(LocalDateTime value) {
        return DATE_TIME_FORMATTER.format(value);
    }
}
