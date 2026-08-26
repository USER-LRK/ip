import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Saves the current task list to the chatbot's data file.
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
}
