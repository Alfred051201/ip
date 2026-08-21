import java.util.ArrayList;
import java.util.Scanner;

public class Dukey {
    private static final String LINE = "____________________________________________________________";

    private static void listAllTasks(ArrayList<String> taskList, ArrayList<Boolean> isDone) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskList.size(); i++) {
            String status = isDone.get(i) ? "X" : " ";
            String line = String.format("%d.[%s] %s", i + 1, status, taskList.get(i));
            System.out.println(line);
        }
    }

    private static void markTask(ArrayList<String> tasks, ArrayList<Boolean> isDone, int taskNumber) {
        isDone.set(taskNumber, true);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  [X] " + tasks.get(taskNumber));
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
        ArrayList<String> tasks = new ArrayList<>();
        tasks.add("read book");
        tasks.add("return book");
        tasks.add("buy bread");

        ArrayList<Boolean> isDone = new ArrayList<>();
        isDone.add(true);
        isDone.add(false);
        isDone.add(false);

        while (conversation && scanner.hasNextLine()) {
            String userInput = scanner.nextLine();

            System.out.println(LINE);

            if (userInput.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                conversation = false;
            } else if (userInput.equals("list")) {
                listAllTasks(tasks, isDone);
            } else if (userInput.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(userInput.substring(5));
                markTask(tasks, isDone, taskNumber - 1);
            } else {
                tasks.add(userInput);
                isDone.add(false);
                System.out.println("added: " + userInput);
            }

            System.out.println(LINE);
        }
    }
}
