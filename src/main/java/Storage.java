import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Saves and loads the current task list from the chatbot's data file.
 */
public final class Storage {
    private static final String DATA_FILE_PATH = "data/kaykay.txt";

    private Storage() {
        // Prevent instantiation of this utility class.
    }

    /**
     * Replaces the data file with the current contents of the task list.
     *
     * @param tasks task list to save
     * @throws IOException if the data directory or file cannot be written
     */
    public static void saveTasks(TaskList tasks) throws IOException {
        File dataFile = new File(DATA_FILE_PATH);
        File dataDirectory = dataFile.getParentFile();
        if (!dataDirectory.exists() && !dataDirectory.mkdirs()) {
            throw new IOException("Could not create the data directory.");
        }

        File temporaryFile = new File(DATA_FILE_PATH + ".tmp");
        try {
            try (FileWriter writer = new FileWriter(temporaryFile)) {
                for (int i = 0; i < tasks.size(); i += 1) {
                    Task task = tasks.getTask(i);
                    writer.write(task.toFileFormat());
                    writer.write(System.lineSeparator());
                }
            }
            try {
                Files.move(temporaryFile.toPath(), dataFile.toPath(),
                        StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException | FileAlreadyExistsException exception) {
                Files.move(temporaryFile.toPath(), dataFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporaryFile.toPath());
        }
    }

    /**
     * Loads tasks from the data file, or returns an empty list if the file does not exist.
     *
     * @return tasks stored in the data file
     * @throws IOException if the data file contains an invalid task or cannot be read
     */
    public static ArrayList<Task> loadTasks() throws IOException {
        File dataFile = new File(DATA_FILE_PATH);
        ArrayList<Task> tasks = new ArrayList<>();
        if (!dataFile.exists()) {
            return tasks;
        }

        try (Scanner scanner = new Scanner(dataFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isBlank()) {
                    tasks.add(parseTask(line));
                }
            }
            if (scanner.ioException() != null) {
                throw scanner.ioException();
            }
        }
        return tasks;
    }

    /**
     * Escapes characters that have a special meaning in the storage format.
     *
     * @param value text to escape
     * @return escaped text
     */
    static String escape(String value) {
        return value.replace("\\", "\\\\")
                .replace("|", "\\|")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }

    /**
     * Creates a task from one line in the data file.
     *
     * @param line serialized task data
     * @return the reconstructed task
     * @throws IOException if the line does not follow the storage format
     */
    private static Task parseTask(String line) throws IOException {
        String[] parts = splitFields(line);
        if (parts.length < 3) {
            throw new IOException("Invalid task data: " + line);
        }

        int status;
        try {
            status = Integer.parseInt(parts[1]);
        } catch (NumberFormatException exception) {
            throw new IOException("Invalid task status: " + line, exception);
        }
        if (status != 0 && status != 1) {
            throw new IOException("Invalid task status: " + line);
        }

        Task task;
        try {
            switch (parts[0]) {
                case "T":
                    if (parts.length != 3) {
                        throw new IOException("Invalid todo data: " + line);
                    }
                    task = new Todo(parts[2]);
                    break;
                case "D":
                    if (parts.length != 4) {
                        throw new IOException("Invalid deadline data: " + line);
                    }
                    task = new Deadline(parts[2], DateTimeParser.parse(parts[3]));
                    break;
                case "E":
                    if (parts.length != 5) {
                        throw new IOException("Invalid event data: " + line);
                    }
                    task = new Event(parts[2], DateTimeParser.parse(parts[3]),
                            DateTimeParser.parse(parts[4]));
                    break;
                default:
                    throw new IOException("Unknown task type: " + line);
            }
        } catch (DateTimeParseException exception) {
            throw new IOException("Invalid date/time data: " + line, exception);
        }

        if (status == 1) {
            task.mark();
        }
        return task;
    }

    /**
     * Splits one storage line at unescaped field separators.
     *
     * @param line serialized task data
     * @return decoded fields from the line
     * @throws IOException if the line ends with an incomplete escape sequence
     */
    private static String[] splitFields(String line) throws IOException {
        ArrayList<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean skipSeparatorSpace = false;

        for (int i = 0; i < line.length(); i += 1) {
            char character = line.charAt(i);
            if (skipSeparatorSpace && character == ' ') {
                skipSeparatorSpace = false;
                continue;
            }
            skipSeparatorSpace = false;

            if (character == '\\') {
                if (i + 1 >= line.length()) {
                    throw new IOException("Incomplete escape sequence: " + line);
                }
                field.append(character);
                field.append(line.charAt(i + 1));
                i += 1;
            } else if (character == '|') {
                removeSeparatorSpace(field);
                fields.add(unescape(field.toString()));
                field.setLength(0);
                skipSeparatorSpace = true;
            } else {
                field.append(character);
            }
        }

        fields.add(unescape(field.toString()));
        return fields.toArray(new String[0]);
    }

    /** Removes the one space belonging to the separator before decoding a field. */
    private static void removeSeparatorSpace(StringBuilder field) {
        if (field.length() > 0 && field.charAt(field.length() - 1) == ' ') {
            field.deleteCharAt(field.length() - 1);
        }
    }

    /** Decodes escaped storage characters. */
    private static String unescape(String value) throws IOException {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < value.length(); i += 1) {
            char character = value.charAt(i);
            if (character != '\\') {
                result.append(character);
                continue;
            }
            if (i + 1 >= value.length()) {
                throw new IOException("Incomplete escape sequence in task data.");
            }
            char escapedCharacter = value.charAt(i + 1);
            switch (escapedCharacter) {
            case 'n':
                result.append('\n');
                break;
            case 'r':
                result.append('\r');
                break;
            case '|':
                result.append('|');
                break;
            case '\\':
                result.append('\\');
                break;
            default:
                result.append('\\').append(escapedCharacter);
                break;
            }
            i += 1;
        }
        return result.toString();
    }
}
