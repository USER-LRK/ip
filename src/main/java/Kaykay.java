import java.util.Scanner;
/**
 * Entry point for the Kaykay chatbot.
 */
public class Kaykay {
    private static final String SEPARATOR = "____________________________________________________________";
    private static final String BANNER = "#   #   ###   #   #  #   #   ###   #   #\n"
            + "#  #   #   #   # #   #  #   #   #   # #\n"
            + "###    #####    #    ###    #####    #\n"
            + "#  #   #   #    #    #  #   #   #    #\n"
            + "#   #  #   #    #    #   #  #   #    #";

    /**
     * Greets the user, repeats user input, and exits when user types bye
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        Task[] tasks = new Task[100];
        int taskCount = 0;
        System.out.println(SEPARATOR);
        System.out.println(BANNER);
        System.out.println(SEPARATOR);
        System.out.println("Hello! I'm kaykay.");
        System.out.println("What can I do for you?");
        System.out.println(SEPARATOR);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            } else if (input.equals("list")) {
                System.out.println(SEPARATOR);
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i += 1) {
                    System.out.printf("%d. %s\n", i + 1, tasks[i]);
                }
                System.out.println(SEPARATOR);
            } else if (input.startsWith("mark ") || input.startsWith("unmark ")) {
                System.out.println(SEPARATOR);
                String[] pieces = input.split(" ");
                int index = Integer.parseInt(pieces[1]) - 1;
                if (pieces[0].equals("mark")) {
                    System.out.println("Nice! I've marked this task as done:");
                    tasks[index].mark();
                } else {
                    System.out.println("OK, I've marked this task as not done yet:");
                    tasks[index].unmark();
                }
                System.out.println(tasks[index]);
                System.out.println(SEPARATOR);
            // AI-GENERATED: Level 4 command parsing and polymorphic task creation.
            } else if (input.startsWith("todo ")) {
                String description = input.substring("todo ".length());
                tasks[taskCount++] = new Todo(description);
                printAddedTask(tasks[taskCount - 1], taskCount);
            } else if (input.startsWith("deadline ")) {
                String[] deadlineParts = input.substring("deadline ".length()).split(" /by ", 2);
                if (deadlineParts.length == 2) {
                    tasks[taskCount++] = new Deadline(deadlineParts[0], deadlineParts[1]);
                    printAddedTask(tasks[taskCount - 1], taskCount);
                } else {
                    printInvalidCommand();
                }
            } else if (input.startsWith("event ")) {
                String eventInput = input.substring("event ".length());
                String[] fromParts = eventInput.split(" /from ", 2);
                if (fromParts.length == 2) {
                    String[] toParts = fromParts[1].split(" /to ", 2);
                    if (toParts.length == 2) {
                        tasks[taskCount++] = new Event(fromParts[0], toParts[0], toParts[1]);
                        printAddedTask(tasks[taskCount - 1], taskCount);
                    } else {
                        printInvalidCommand();
                    }
                } else {
                    printInvalidCommand();
                }
            } else {
                System.out.println(SEPARATOR);
                System.out.println("Got it. I've added this task:");
                tasks[taskCount++] = new Todo(input);
                System.out.println(tasks[taskCount - 1]);
                System.out.printf("Now you have %d tasks in the list.\n", taskCount);
                System.out.println(SEPARATOR);
            }
        }
        System.out.println(SEPARATOR);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(SEPARATOR);
    }

    // AI-GENERATED: Shared output helper for newly supported task commands.
    private static void printAddedTask(Task task, int taskCount) {
        System.out.println(SEPARATOR);
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        System.out.printf("Now you have %d tasks in the list.\n", taskCount);
        System.out.println(SEPARATOR);
    }

    // AI-GENERATED: Keeps malformed Level 4 commands from terminating the chatbot.
    private static void printInvalidCommand() {
        System.out.println(SEPARATOR);
        System.out.println("Please use: deadline <description> /by <date/time> or event <description> /from <start> /to <end>.");
        System.out.println(SEPARATOR);
    }
}
