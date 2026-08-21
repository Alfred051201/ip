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

    private static void getTaskAmount(ArrayList<Task> tasks) {
        System.out.println(String.format("Now you have %d tasks in the list", tasks.size()));
    }

    private static String[] parseByKeywords(String input, String... keywords) {
        String[] result = new String[keywords.length + 1];

        int currentStart = 0;

        for (int i = 0; i < keywords.length; i++) {
            String keyword = " " + keywords[i] + " ";
            int keywordIndex = input.indexOf(keyword, currentStart);

            result[i] = input.substring(currentStart, keywordIndex).trim();
            currentStart = keywordIndex + keyword.length();
        }

        result[keywords.length] = input.substring(currentStart).trim();
        return result;
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
        Task t = new Todo("read book");
        t.markAsDone();
        tasks.add(t);
        tasks.add(new Deadline("return book", "June 6th"));
        tasks.add(new Event("project meeting", "Aug 6th 2pm", "4pm"));
        Task t_1 = new Todo("join sports club");
        t_1.markAsDone();
        tasks.add(t_1);

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
            } else if (userInput.startsWith("todo ")) {
                String todoName = userInput.substring(5);
                Task newTask = new Todo(todoName);
                tasks.add(newTask);
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + newTask);
                getTaskAmount(tasks);
            } else if (userInput.startsWith("deadline ")) {
                String input = userInput.substring(9); // after "deadline "
                String[] parts = parseByKeywords(input, "/by");

                String description = parts[0];
                String by = parts[1];

                Task newTask = new Deadline(description, by);
                tasks.add(newTask);
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + newTask);
                getTaskAmount(tasks);
            } else if (userInput.startsWith("event ")) {
                String input = userInput.substring(6); // after "event "
                String[] parts = parseByKeywords(input, "/from", "/to");

                String description = parts[0];
                String from = parts[1];
                String to = parts[2];

                Task newTask = new Event(description, from, to);
                tasks.add(newTask);
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + newTask);
                getTaskAmount(tasks);
            }
            else {
                tasks.add(new Task(userInput));
                System.out.println("added: " + userInput);
            }

            System.out.println(LINE);
        }
    }
}
