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

    private static String[] parseByKeywords(String input, String errorMessage, String... keywords) throws DukeyException {
        String[] result = new String[keywords.length + 1];

        int currentStart = 0;

        for (int i = 0; i < keywords.length; i++) {
            String keyword = keywords[i];

            int keywordIndex = input.indexOf(keyword, currentStart);

            if (keywordIndex == -1) {
                throw new DukeyException(errorMessage);
            }

            while (keywordIndex != -1
                    && ((keywordIndex > 0 && input.charAt(keywordIndex - 1) != ' ')
                    || (keywordIndex + keyword.length() < input.length()
                    && input.charAt(keywordIndex + keyword.length()) != ' '))) {
                keywordIndex = input.indexOf(keyword, keywordIndex + 1);
            }

            if (keywordIndex == -1) {
                throw new DukeyException(errorMessage);
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
                try {
                    String taskNumberText = userInput.substring(4).trim();
                    if (taskNumberText.isEmpty()) {
                        throw new DukeyException("Please provide a task number to mark.");
                    }

                    int taskNumber = Integer.parseInt(taskNumberText);
                    if (taskNumber < 1 || taskNumber > tasks.size()) {
                        throw new DukeyException("Please provide a valid task number.");
                    }

                    Task task = tasks.get(taskNumber - 1);
                    task.markAsDone();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + task);
                } catch (NumberFormatException e) {
                    System.out.println(" OOPS!!! Please provide a valid task number.");
                } catch (DukeyException e) {
                    System.out.println(" OOPS!!! " + e.getMessage());
                }
            } else if (userInput.startsWith("unmark")) {
                try {
                    String taskNumberText = userInput.substring(6).trim();
                    if (taskNumberText.isEmpty()) {
                        throw new DukeyException("Please provide a task number to unmark.");
                    }

                    int taskNumber = Integer.parseInt(taskNumberText);
                    if (taskNumber < 1 || taskNumber > tasks.size()) {
                        throw new DukeyException("Please provide a valid task number.");
                    }

                    Task task = tasks.get(taskNumber - 1);
                    task.markAsUndone();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + task);
                } catch (NumberFormatException e) {
                    System.out.println(" OOPS!!! Please provide a valid task number.");
                } catch (DukeyException e) {
                    System.out.println(" OOPS!!! " + e.getMessage());
                }
            } else if (userInput.startsWith("todo")) {
                try {
                    String todoName = userInput.substring(4).trim();

                    if (todoName.isEmpty()) {
                        throw new DukeyException("The description of a todo cannot be empty.");
                    }

                    Task newTask = new Todo(todoName);
                    tasks.add(newTask);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + newTask);
                    getTaskAmount(tasks);

                } catch (DukeyException e) {
                    System.out.println(" OOPS!!! " + e.getMessage());
                }

            } else if (userInput.startsWith("deadline")) {
                try {
                    String input = userInput.substring(8).trim();
                    if (input.isEmpty()) {
                        throw new DukeyException("Please provide a deadline task description.");
                    }
                    String[] parts = parseByKeywords(input, "Please use: deadline {DESCRIPTION} /by {WHEN}", "/by");

                    if (parts[0].isEmpty()) {
                        throw new DukeyException("Please provide a deadline task description.");
                    }

                    if (parts[1].isEmpty()) {
                        throw new DukeyException("Please provide a deadline task date/time after /by.");
                    }

                    Task newTask = new Deadline(parts[0], parts[1]);
                    tasks.add(newTask);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + newTask);
                    getTaskAmount(tasks);

                } catch (DukeyException e) {
                    System.out.println(" OOPS!!! " + e.getMessage());
                }

            } else if (userInput.startsWith("event")) {
                try {
                    String input = userInput.substring(5).trim();
                    if (input.isEmpty()) {
                        throw new DukeyException("Please provide a event task description.");
                    }
                    String[] parts = parseByKeywords(input, "Please use: event {DESCRIPTION} /from {WHEN} /to {WHEN}",
                            "/from", "/to");

                    if (parts[0].isEmpty()) {
                        throw new DukeyException("Please provide a event task description.");
                    }

                    if (parts[1].isEmpty()) {
                        throw new DukeyException("Please provide a event task date/time after /from.");
                    }

                    if (parts[2].isEmpty()) {
                        throw new DukeyException("Please provide a event task date/time after /to.");
                    }

                    Task newTask = new Event(parts[0], parts[1], parts[2]);
                    tasks.add(newTask);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + newTask);
                    getTaskAmount(tasks);

                } catch (DukeyException e) {
                    System.out.println(" OOPS!!! " + e.getMessage());
                }
            } else {
                System.out.println(" OOPS!!! I'm sorry, but I don't know what that means :-(");
            }

            System.out.println(LINE);
        }
    }
}
