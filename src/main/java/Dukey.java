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
        System.out.println(String.format("Now you have %d tasks in the list.", tasks.size()));
    }

    private static String[] parseByKeywords(String input, String... keywords) {
        String[] result = new String[keywords.length + 1];

        int currentStart = 0;

        for (int i = 0; i < keywords.length; i++) {
            String keyword = keywords[i];

            int keywordIndex = input.indexOf(keyword, currentStart);
            while (keywordIndex != -1
                    && ((keywordIndex > 0 && input.charAt(keywordIndex - 1) != ' ')
                    || (keywordIndex + keyword.length() < input.length()
                    && input.charAt(keywordIndex + keyword.length()) != ' '))) {
                keywordIndex = input.indexOf(keyword, keywordIndex + 1);
            }

            if (keywordIndex == -1) {
                return null;
            }

            result[i] = input.substring(currentStart, keywordIndex).trim();
            currentStart = keywordIndex + keyword.length();
            while (currentStart < input.length() && input.charAt(currentStart) == ' ') {
                currentStart++;
            }
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
            } else if (userInput.startsWith("mark")) {
                int taskNumber = Integer.parseInt(userInput.substring(4).strip());
                Task task = tasks.get(taskNumber - 1);
                task.markAsDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + task);
            } else if (userInput.startsWith("unmark")) {
                int taskNumber = Integer.parseInt(userInput.substring(6).strip());
                Task task = tasks.get(taskNumber - 1);
                task.markAsUndone();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + task);
            } else if (userInput.startsWith("todo")) {
                String todoName = userInput.substring(4).strip();
                if (todoName.isEmpty()) {
                    System.out.println(" OOPS!!! The description of a todo cannot be empty.");
                } else {
                    Task newTask = new Todo(todoName);
                    tasks.add(newTask);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + newTask);
                    getTaskAmount(tasks);
                }
            } else if (userInput.startsWith("deadline")) {
                String input = userInput.substring(8).trim(); // after "deadline "
                if (input.isEmpty()) {
                    System.out.println(" OOPS!!! Please provide a deadline task description.");
                } else {
                    String[] parts = parseByKeywords(input, "/by");

                    if (parts == null) {
                        System.out.println(" OOPS!!! Please use: deadline DESCRIPTION /by WHEN");
                    } else if (parts[0].isEmpty()) {
                        System.out.println(" OOPS!!! Please provide a deadline task description.");
                    } else if (parts[1].isEmpty()) {
                        System.out.println(" OOPS!!! Please provide a deadline task date/time after /by.");
                    } else {
                        Task newTask = new Deadline(parts[0], parts[1]);
                        tasks.add(newTask);
                        System.out.println("Got it. I've added this task:");
                        System.out.println("  " + newTask);
                        getTaskAmount(tasks);
                    }
                }

            } else if (userInput.startsWith("event")) {
                String input = userInput.substring(5).trim(); // after "event "
                if (input.isEmpty()) {
                    System.out.println(" OOPS!!! Please provide a event task description.");
                } else {
                    String[] parts = parseByKeywords(input, "/from", "/to");

                    if (parts == null) {
                        System.out.println(" OOPS!!! Please use: event DESCRIPTION /from WHEN /to WHEN");
                    } else if (parts[0].isEmpty()) {
                        System.out.println(" OOPS!!! Please provide a event task description.");
                    } else if (parts[1].isEmpty()) {
                        System.out.println(" OOPS!!! Please provide a event task date/time after /from.");
                    } else if (parts[2].isEmpty()) {
                        System.out.println(" OOPS!!! Please provide a event task date/time after /to.");
                    } else {
                        Task newTask = new Event(parts[0], parts[1], parts[2]);
                        tasks.add(newTask);
                        System.out.println("Got it. I've added this task:");
                        System.out.println("  " + newTask);
                        getTaskAmount(tasks);
                    }
                }
            } else {
                System.out.println(" OOPS!!! I'm sorry, but I don't know what that means :-(");
            }

            System.out.println(LINE);
        }
    }
}
