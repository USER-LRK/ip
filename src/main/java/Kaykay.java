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
            } else if (input.contains("mark")) {
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
            }  else {
                System.out.println(SEPARATOR);
                System.out.printf("added: %s\n", input);
                System.out.println(SEPARATOR);
                tasks[taskCount++] = new Task(input);
            }
        }
        System.out.println(SEPARATOR);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(SEPARATOR);
    }
}
