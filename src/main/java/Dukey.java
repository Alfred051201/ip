import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
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

    private static void listTasksOnDate(ArrayList<Task> tasks, LocalDate date) {
        boolean hasMatchingTask = false;

        System.out.println("Here are the deadlines and events on that date:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.occursOn(date)) {
                System.out.println(String.format("%d.%s", i + 1, task));
                hasMatchingTask = true;
            }
        }

        if (!hasMatchingTask) {
            System.out.println("There are no deadlines or events on that date.");
        }
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
            try {
                String[] parts = s.nextLine().split("\\s*\\|\\s*");

                if (!Arrays.asList("T", "D", "E").contains(parts[0])) {
                    throw new DukeyException("Undefined task type.");
                }

                if (parts.length < 3) {
                    throw new DukeyException("Saved task is missing fields.");
                }

                if (parts[1].isEmpty()) {
                    throw new DukeyException("Done status is missing for this task.");
                }

                if (!Arrays.asList("0", "1").contains(parts[1])) {
                    throw new DukeyException("Undefined done status.");
                }

                if (parts[2].isEmpty()) {
                    throw new DukeyException("Description is missing for this task.");
                }

                Task newTask;

                if (parts[0].equals("T")) {
                    if (parts[2].isEmpty()) {
                        throw new DukeyException("Description is missing for this task.");
                    }
                    newTask = new Todo(parts[2]);
                } else if (parts[0].equals("D")) {
                    if (parts.length < 4 || parts[3].isEmpty()) {
                        throw new DukeyException("Deadline date/time is missing for this task.");
                    }
                    newTask = new Deadline(parts[2], parts[3]);
                } else {
                    if (parts.length < 5 || parts[3].isEmpty() || parts[4].isEmpty()) {
                        throw new DukeyException("Event date/time is missing for this task.");
                    }
                    newTask = new Event(parts[2], parts[3], parts[4]);
                }

                if (parts[1].equals("1")) {
                    newTask.markAsDone();
                }
                tasks.add(newTask);

            } catch (DateTimeParseException e) {
                System.out.println(" OOPS!!! Saved date/time must use format: yyyy-MM-dd HHmm");
            } catch (Exception e) {
                System.out.println(" OOPS!!! " + e.getMessage());
            }
        }
    }

    private static void writeToTaskFile(String filePath, ArrayList<Task> tasks) throws DukeyException {
        File file = new File(filePath);
        File parentDirectory = file.getParentFile();

        if (parentDirectory != null && !parentDirectory.exists()) {
            boolean isParentDirCreated = parentDirectory.mkdirs();

            if (!isParentDirCreated) {
                throw new DukeyException("Could not create data directory");
            }
        }

        try (FileWriter fw = new FileWriter(file)) {
            for (Task task : tasks) {

                String fileLine = task.toFileString();

                if (fileLine.isEmpty()) {
                    throw new DukeyException("Could not save an unknown task type.");
                }
                fw.write(fileLine);
                fw.write(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new DukeyException("Could not save tasks to file.");
        }
    }

    private static void saveTasks(String taskFilePath, ArrayList<Task> tasks) {
        try {
            writeToTaskFile(taskFilePath, tasks);
        } catch (DukeyException e) {
            System.out.println(" OOPS!!! " + e.getMessage());
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
        String taskFilePath = "src/main/data/dukey.txt";
        try {
            loadFileTasks(tasks, taskFilePath);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        while (conversation && scanner.hasNextLine()) {
            String userInput = scanner.nextLine();

            System.out.println(LINE);

            if (isCommand(userInput, Command.BYE)) {
                System.out.println("Bye. Hope to see you again soon!");
                saveTasks(taskFilePath, tasks);
                conversation = false;
            } else if (isCommand(userInput, Command.LIST)) {
                listAllTasks(tasks);
            } else if (isCommand(userInput, Command.ON)) {
                try {
                    String dateText = userInput.substring(2).trim();
                    if (dateText.isEmpty()) {
                        throw new DukeyException("Please provide a date using format: yyyy-MM-dd");
                    }

                    LocalDate date = LocalDate.parse(dateText);
                    listTasksOnDate(tasks, date);
                } catch (DukeyException e) {
                    System.out.println(" OOPS!!! " + e.getMessage());
                } catch (DateTimeParseException e) {
                    System.out.println(" OOPS!!! Please use date format: yyyy-MM-dd");
                }
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
                    saveTasks(taskFilePath, tasks);
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
                    saveTasks(taskFilePath, tasks);
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
                    saveTasks(taskFilePath, tasks);
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
                    saveTasks(taskFilePath, tasks);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + newTask);
                    getTaskAmount(tasks);

                } catch (DukeyException e) {
                    System.out.println(" OOPS!!! " + e.getMessage());
                } catch (DateTimeParseException e) {
                    System.out.println(" OOPS!!! Please use deadline date/time format: yyyy-MM-dd HHmm");
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
                    saveTasks(taskFilePath, tasks);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + newTask);
                    getTaskAmount(tasks);

                } catch (DukeyException e) {
                    System.out.println(" OOPS!!! " + e.getMessage());
                } catch (DateTimeParseException e) {
                    System.out.println(" OOPS!!! Please use event date/time format: yyyy-MM-dd HHmm");
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
                    saveTasks(taskFilePath, tasks);
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
