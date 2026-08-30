import java.io.FileNotFoundException;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Dukey {
    private static void saveTasks(Storage storage, TaskList tasks, Ui ui) {
        try {
            storage.save(tasks);
        } catch (DukeyException e) {
            ui.showError(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Ui ui = new Ui();
        Parser parser = new Parser();
        ui.showWelcome();

        boolean conversation = true;
        Scanner scanner = new Scanner(System.in);
        TaskList tasks = new TaskList();

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
            Command command = parser.parseCommand(userInput);

            ui.showLine();

            if (command == Command.BYE) {
                ui.showBye();
                saveTasks(storage, tasks, ui);
                conversation = false;
            } else if (command == Command.LIST) {
                ui.showList(tasks);
            } else if (command == Command.ON) {
                try {
                    ui.showTasksOnDate(tasks, parser.parseOnDate(userInput));
                } catch (DukeyException e) {
                    ui.showError(e.getMessage());
                } catch (DateTimeParseException e) {
                    ui.showError("Please use date format: yyyy-MM-dd");
                }
            } else if (command == Command.MARK) {
                try {
                    int taskNumber = parser.parseTaskNumber(userInput, Command.MARK,
                            "Please provide a task number to mark.");
                    if (!tasks.isValidTaskNumber(taskNumber)) {
                        throw new DukeyException("Please provide a valid task number.");
                    }

                    Task task = tasks.get(taskNumber);
                    task.markAsDone();
                    saveTasks(storage, tasks, ui);
                    ui.showTaskMarked(task);
                } catch (NumberFormatException e) {
                    ui.showError("Please provide a valid task number.");
                } catch (DukeyException e) {
                    ui.showError(e.getMessage());
                }
            } else if (command == Command.UNMARK) {
                try {
                    int taskNumber = parser.parseTaskNumber(userInput, Command.UNMARK,
                            "Please provide a task number to unmark.");
                    if (!tasks.isValidTaskNumber(taskNumber)) {
                        throw new DukeyException("Please provide a valid task number.");
                    }

                    Task task = tasks.get(taskNumber);
                    task.markAsUndone();
                    saveTasks(storage, tasks, ui);
                    ui.showTaskUnmarked(task);
                } catch (NumberFormatException e) {
                    ui.showError("Please provide a valid task number.");
                } catch (DukeyException e) {
                    ui.showError(e.getMessage());
                }
            } else if (command == Command.TODO) {
                try {
                    Task newTask = new Todo(parser.parseTodoDescription(userInput));
                    tasks.add(newTask);
                    saveTasks(storage, tasks, ui);
                    ui.showTaskAdded(newTask, tasks);

                } catch (DukeyException e) {
                    ui.showError(e.getMessage());
                }

            } else if (command == Command.DEADLINE) {
                try {
                    String[] parts = parser.parseDeadline(userInput);
                    Task newTask = new Deadline(parts[0], parts[1]);
                    tasks.add(newTask);
                    saveTasks(storage, tasks, ui);
                    ui.showTaskAdded(newTask, tasks);

                } catch (DukeyException e) {
                    ui.showError(e.getMessage());
                } catch (DateTimeParseException e) {
                    ui.showError("Please use deadline date/time format: yyyy-MM-dd HHmm");
                }

            } else if (command == Command.EVENT) {
                try {
                    String[] parts = parser.parseEvent(userInput);
                    Task newTask = new Event(parts[0], parts[1], parts[2]);
                    tasks.add(newTask);
                    saveTasks(storage, tasks, ui);
                    ui.showTaskAdded(newTask, tasks);

                } catch (DukeyException e) {
                    ui.showError(e.getMessage());
                } catch (DateTimeParseException e) {
                    ui.showError("Please use event date/time format: yyyy-MM-dd HHmm");
                }
            } else if (command == Command.DELETE) {
                try {
                    int taskNumber = parser.parseTaskNumber(userInput, Command.DELETE,
                            "Please provide a task number to delete.");
                    if (!tasks.isValidTaskNumber(taskNumber)) {
                        throw new DukeyException("Please provide a valid task number.");
                    }

                    Task task = tasks.delete(taskNumber);
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
