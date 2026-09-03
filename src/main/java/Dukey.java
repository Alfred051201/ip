import java.io.FileNotFoundException;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Dukey {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;
    private Parser parser;

    public Dukey(String filePath) {
        this.ui = new Ui();
        this.parser = new Parser();
        this.ui.showWelcome();

        this.storage = new Storage(filePath);
        try {
            this.tasks = storage.load();
        } catch (FileNotFoundException e) {
            this.ui.showLoadingError();
            this.tasks = new TaskList();
        } catch (DukeyException e) {
            this.ui.showError(e.getMessage());
            this.tasks = new TaskList();
        }
    }

    private void saveTasks() {
        try {
            this.storage.save(this.tasks);
        } catch (DukeyException e) {
            this.ui.showError(e.getMessage());
        }
    }

    public void run() {
        boolean conversation = true;
        Scanner scanner = new Scanner(System.in);

        while (conversation && scanner.hasNextLine()) {
            String userInput = scanner.nextLine();
            CommandWord command = this.parser.parseCommand(userInput);

            this.ui.showLine();

            if (command == CommandWord.BYE) {
                try {
                    Command exitCommand = this.parser.parse(userInput);
                    exitCommand.execute(this.tasks, this.ui, this.storage);
                    conversation = !exitCommand.isExit();
                } catch (DukeyException e) {
                    this.ui.showError(e.getMessage());
                }
            } else if (command == CommandWord.LIST) {
                this.ui.showList(this.tasks);
            } else if (command == CommandWord.ON) {
                try {
                    this.ui.showTasksOnDate(this.tasks, this.parser.parseOnDate(userInput));
                } catch (DukeyException e) {
                    this.ui.showError(e.getMessage());
                } catch (DateTimeParseException e) {
                    this.ui.showError("Please use date format: yyyy-MM-dd");
                }
            } else if (command == CommandWord.MARK) {
                try {
                    int taskNumber = this.parser.parseTaskNumber(userInput, CommandWord.MARK,
                            "Please provide a task number to mark.");
                    if (!this.tasks.isValidTaskNumber(taskNumber)) {
                        throw new DukeyException("Please provide a valid task number.");
                    }

                    Task task = this.tasks.get(taskNumber);
                    task.markAsDone();
                    saveTasks();
                    this.ui.showTaskMarked(task);
                } catch (NumberFormatException e) {
                    this.ui.showError("Please provide a valid task number.");
                } catch (DukeyException e) {
                    this.ui.showError(e.getMessage());
                }
            } else if (command == CommandWord.UNMARK) {
                try {
                    int taskNumber = this.parser.parseTaskNumber(userInput, CommandWord.UNMARK,
                            "Please provide a task number to unmark.");
                    if (!this.tasks.isValidTaskNumber(taskNumber)) {
                        throw new DukeyException("Please provide a valid task number.");
                    }

                    Task task = this.tasks.get(taskNumber);
                    task.markAsUndone();
                    saveTasks();
                    this.ui.showTaskUnmarked(task);
                } catch (NumberFormatException e) {
                    this.ui.showError("Please provide a valid task number.");
                } catch (DukeyException e) {
                    this.ui.showError(e.getMessage());
                }
            } else if (command == CommandWord.TODO) {
                try {
                    Task newTask = new Todo(this.parser.parseTodoDescription(userInput));
                    this.tasks.add(newTask);
                    saveTasks();
                    this.ui.showTaskAdded(newTask, this.tasks);

                } catch (DukeyException e) {
                    this.ui.showError(e.getMessage());
                }

            } else if (command == CommandWord.DEADLINE) {
                try {
                    String[] parts = this.parser.parseDeadline(userInput);
                    Task newTask = new Deadline(parts[0], parts[1]);
                    this.tasks.add(newTask);
                    saveTasks();
                    this.ui.showTaskAdded(newTask, this.tasks);

                } catch (DukeyException e) {
                    this.ui.showError(e.getMessage());
                } catch (DateTimeParseException e) {
                    this.ui.showError("Please use deadline date/time format: yyyy-MM-dd HHmm");
                }

            } else if (command == CommandWord.EVENT) {
                try {
                    String[] parts = this.parser.parseEvent(userInput);
                    Task newTask = new Event(parts[0], parts[1], parts[2]);
                    this.tasks.add(newTask);
                    saveTasks();
                    this.ui.showTaskAdded(newTask, this.tasks);

                } catch (DukeyException e) {
                    this.ui.showError(e.getMessage());
                } catch (DateTimeParseException e) {
                    this.ui.showError("Please use event date/time format: yyyy-MM-dd HHmm");
                }
            } else if (command == CommandWord.DELETE) {
                try {
                    int taskNumber = this.parser.parseTaskNumber(userInput, CommandWord.DELETE,
                            "Please provide a task number to delete.");
                    if (!this.tasks.isValidTaskNumber(taskNumber)) {
                        throw new DukeyException("Please provide a valid task number.");
                    }

                    Task task = this.tasks.delete(taskNumber);
                    saveTasks();
                    this.ui.showTaskDeleted(task, this.tasks);
                } catch (NumberFormatException e) {
                    this.ui.showError("Please provide a valid task number.");
                } catch (DukeyException e) {
                    this.ui.showError(e.getMessage());
                }
            } else {
                this.ui.showError("I'm sorry, but I don't know what that means :-(");
            }

            this.ui.showLine();
        }
    }

    public static void main(String[] args) {
        new Dukey("src/main/data/dukey.txt").run();
    }
}
