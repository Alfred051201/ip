import java.util.ArrayList;
import java.util.Scanner;

public class Dukey {
    private static final String LINE = "____________________________________________________________";

    private static void listAllTasks(ArrayList<Task> tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(String.format("%d.%s", i + 1, tasks.get(i)));
        }
    }

    public static void main(String[] args) {
        String banner = " ____        _              \n"
                + "|  _ \\ _   _| | _____ _   _ \n"
                + "| | | | | | | |/ / _ \\ | | |\n"
                + "| |_| | |_| |   <  __/ |_| |\n"
                + "|____/ \\__,_|_|\\_\\___|\\__, |\n"
                + "                       |___/ \n";
        System.out.println(LINE);
        System.out.println(banner);
        System.out.println("Hello! I'm Dukey.");
        System.out.println("What can I do for you?");
        System.out.println(LINE);

        boolean conversation = true;
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Task("read book", true));
        tasks.add(new Task("return book"));
        tasks.add(new Task("buy bread"));

        while (conversation && scanner.hasNextLine()) {
            String userInput = scanner.nextLine();

            System.out.println(LINE);

            if (userInput.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                conversation = false;
            } else if (userInput.equals("list")) {
                listAllTasks(tasks);
            } else if (userInput.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(userInput.substring(5));
                Task task = tasks.get(taskNumber - 1);
                task.markAsDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + task);
            } else if (userInput.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(userInput.substring(7));
                Task task = tasks.get(taskNumber - 1);
                task.markAsUndone();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + task);
            } else {
                tasks.add(new Task(userInput));
                System.out.println("added: " + userInput);
            }

            System.out.println(LINE);
        }
    }
}
