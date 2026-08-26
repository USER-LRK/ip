import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
     * @param tasks tasks to save
     * @throws IOException if the data directory or file cannot be written
     */
    public static void saveTasks(List<Task> tasks) throws IOException {
        File dataFile = new File(DATA_FILE_PATH);
        File dataDirectory = dataFile.getParentFile();
        if (!dataDirectory.exists() && !dataDirectory.mkdirs()) {
            throw new IOException("Could not create the data directory.");
        }

        try (FileWriter writer = new FileWriter(dataFile)) {
            for (Task task : tasks) {
                writer.write(task.toFileFormat());
                writer.write(System.lineSeparator());
            }
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
        }
        return tasks;
    }

    /**
     * Creates a task from one line in the data file.
     *
     * @param line serialized task data
     * @return the reconstructed task
     * @throws IOException if the line does not follow the storage format
     */
    private static Task parseTask(String line) throws IOException {
        String[] parts = line.split(" \\| ", -1);
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
            task = new Deadline(parts[2], parts[3]);
            break;
        case "E":
            if (parts.length != 5) {
                throw new IOException("Invalid event data: " + line);
            }
            task = new Event(parts[2], parts[3], parts[4]);
            break;
        default:
            throw new IOException("Unknown task type: " + line);
        }

        if (status == 1) {
            task.mark();
        }
        return task;
    }
}
