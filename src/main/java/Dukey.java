import java.io.File;
import java.io.FileNotFoundException;
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

    private static boolean isCommand(String userInput, Command command) {
        String commandWord = command.getWord();
        return userInput.equals(commandWord) || userInput.startsWith(commandWord + " ");
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

    private static void loadFileTasks(ArrayList<Task> tasks, String filePath) throws FileNotFoundException {
        File f = new File(filePath); // create a File for the given file path
        Scanner s = new Scanner(f); // create a Scanner using the File as the source
        while (s.hasNext()) {
            String[] parts = s.nextLine().split(" \\| ");

            if (parts[0].equals("T")) {
                Task newTask = new Todo(parts[2]);
                if (parts[1].equals("1")) {
                    newTask.markAsDone();
                }
                tasks.add(newTask);
            } else if (parts[0].equals("D")) {
                Task newTask = new Deadline(parts[2], parts[3]);
                if (parts[1].equals("1")) {
                    newTask.markAsDone();
                }
                tasks.add(newTask);
            } else if (parts[0].equals("E")) {
                String[] subparts = parts[3].split("-");
                Task newTask = new Event(parts[2], subparts[0], subparts[1]);
                if (parts[1].equals("1")) {
                    newTask.markAsDone();
                }
                tasks.add(newTask);
            } else {
                System.out.println("No relevant command");
            }
            // System.out.println(s.nextLine());
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

        // access file content and return error if file not found
        try {
            loadFileTasks(tasks, "src/main/data/dukey.txt");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        while (conversation && scanner.hasNextLine()) {
            String userInput = scanner.nextLine();

            System.out.println(LINE);

            if (isCommand(userInput, Command.BYE)) {
                System.out.println("Bye. Hope to see you again soon!");
                conversation = false;
            } else if (isCommand(userInput, Command.LIST)) {
                listAllTasks(tasks);
            } else if (isCommand(userInput, Command.MARK)) {
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
            } else if (isCommand(userInput, Command.UNMARK)) {
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
            } else if (isCommand(userInput, Command.TODO)) {
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

            } else if (isCommand(userInput, Command.DEADLINE)) {
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

            } else if (isCommand(userInput, Command.EVENT)) {
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
            } else if (isCommand(userInput, Command.DELETE)) {
                try {
                    String taskNumberText = userInput.substring(6).trim();
                    if (taskNumberText.isEmpty()) {
                        throw new DukeyException("Please provide a task number to delete.");
                    }

                    int taskNumber = Integer.parseInt(taskNumberText);
                    if (taskNumber < 1 || taskNumber > tasks.size()) {
                        throw new DukeyException("Please provide a valid task number.");
                    }

                    Task task = tasks.get(taskNumber - 1);
                    tasks.remove(taskNumber - 1);
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + task);
                    getTaskAmount(tasks);
                } catch (NumberFormatException e) {
                    System.out.println(" OOPS!!! Please provide a valid task number.");
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
