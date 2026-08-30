import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Dukey {
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

    private static void saveTasks(Storage storage, ArrayList<Task> tasks, Ui ui) {
        try {
            storage.save(tasks);
        } catch (DukeyException e) {
            ui.showError(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        boolean conversation = true;
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();

        Storage storage = new Storage("src/main/data/dukey.txt");
        try {
            tasks = storage.load();
        } catch (FileNotFoundException e) {
            ui.showLoadingError();
        } catch (DukeyException e) {
            ui.showError(e.getMessage());
        }

        while (conversation && scanner.hasNextLine()) {
            String userInput = scanner.nextLine();

            ui.showLine();

            if (isCommand(userInput, Command.BYE)) {
                ui.showBye();
                saveTasks(storage, tasks, ui);
                conversation = false;
            } else if (isCommand(userInput, Command.LIST)) {
                ui.showList(tasks);
            } else if (isCommand(userInput, Command.ON)) {
                try {
                    String dateText = userInput.substring(2).trim();
                    if (dateText.isEmpty()) {
                        throw new DukeyException("Please provide a date using format: yyyy-MM-dd");
                    }

                    LocalDate date = LocalDate.parse(dateText);
                    ui.showTasksOnDate(tasks, date);
                } catch (DukeyException e) {
                    ui.showError(e.getMessage());
                } catch (DateTimeParseException e) {
                    ui.showError("Please use date format: yyyy-MM-dd");
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
                    saveTasks(storage, tasks, ui);
                    ui.showTaskMarked(task);
                } catch (NumberFormatException e) {
                    ui.showError("Please provide a valid task number.");
                } catch (DukeyException e) {
                    ui.showError(e.getMessage());
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
                    saveTasks(storage, tasks, ui);
                    ui.showTaskUnmarked(task);
                } catch (NumberFormatException e) {
                    ui.showError("Please provide a valid task number.");
                } catch (DukeyException e) {
                    ui.showError(e.getMessage());
                }
            } else if (isCommand(userInput, Command.TODO)) {
                try {
                    String todoName = userInput.substring(4).trim();

                    if (todoName.isEmpty()) {
                        throw new DukeyException("The description of a todo cannot be empty.");
                    }

                    Task newTask = new Todo(todoName);
                    tasks.add(newTask);
                    saveTasks(storage, tasks, ui);
                    ui.showTaskAdded(newTask, tasks);

                } catch (DukeyException e) {
                    ui.showError(e.getMessage());
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
                    saveTasks(storage, tasks, ui);
                    ui.showTaskAdded(newTask, tasks);

                } catch (DukeyException e) {
                    ui.showError(e.getMessage());
                } catch (DateTimeParseException e) {
                    ui.showError("Please use deadline date/time format: yyyy-MM-dd HHmm");
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
                    saveTasks(storage, tasks, ui);
                    ui.showTaskAdded(newTask, tasks);

                } catch (DukeyException e) {
                    ui.showError(e.getMessage());
                } catch (DateTimeParseException e) {
                    ui.showError("Please use event date/time format: yyyy-MM-dd HHmm");
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
                    saveTasks(storage, tasks, ui);
                    ui.showTaskDeleted(task, tasks);
                } catch (NumberFormatException e) {
                    ui.showError("Please provide a valid task number.");
                } catch (DukeyException e) {
                    ui.showError(e.getMessage());
                }
            } else {
                ui.showError("I'm sorry, but I don't know what that means :-(");
            }

            ui.showLine();
        }
    }
}
