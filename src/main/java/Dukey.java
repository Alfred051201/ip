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
                try {
                    Command listCommand = this.parser.parse(userInput);
                    listCommand.execute(this.tasks, this.ui, this.storage);
                } catch (DukeyException e) {
                    this.ui.showError(e.getMessage());
                }
            } else if (command == CommandWord.ON) {
                try {
                    Command onCommand = this.parser.parse(userInput);
                    onCommand.execute(this.tasks, this.ui, this.storage);
                } catch (DukeyException e) {
                    this.ui.showError(e.getMessage());
                } catch (DateTimeParseException e) {
                    this.ui.showError("Please use date format: yyyy-MM-dd");
                }
            } else if (command == CommandWord.MARK) {
                try {
                    Command markCommand = this.parser.parse(userInput);
                    markCommand.execute(this.tasks, this.ui, this.storage);
                } catch (NumberFormatException e) {
                    this.ui.showError("Please provide a valid task number.");
                } catch (DukeyException e) {
                    this.ui.showError(e.getMessage());
                }
            } else if (command == CommandWord.UNMARK) {
                try {
                    Command unmarkCommand = this.parser.parse(userInput);
                    unmarkCommand.execute(this.tasks, this.ui, this.storage);
                } catch (NumberFormatException e) {
                    this.ui.showError("Please provide a valid task number.");
                } catch (DukeyException e) {
                    this.ui.showError(e.getMessage());
                }
            } else if (command == CommandWord.TODO) {
                try {
                    Command todoCommand = this.parser.parse(userInput);
                    todoCommand.execute(this.tasks, this.ui, this.storage);

                } catch (DukeyException e) {
                    this.ui.showError(e.getMessage());
                }

            } else if (command == CommandWord.DEADLINE) {
                try {
                    Command deadlineCommand = this.parser.parse(userInput);
                    deadlineCommand.execute(this.tasks, this.ui, this.storage);

                } catch (DukeyException e) {
                    this.ui.showError(e.getMessage());
                } catch (DateTimeParseException e) {
                    this.ui.showError("Please use deadline date/time format: yyyy-MM-dd HHmm");
                }

            } else if (command == CommandWord.EVENT) {
                try {
                    Command eventCommand = this.parser.parse(userInput);
                    eventCommand.execute(this.tasks, this.ui, this.storage);

                } catch (DukeyException e) {
                    this.ui.showError(e.getMessage());
                } catch (DateTimeParseException e) {
                    this.ui.showError("Please use event date/time format: yyyy-MM-dd HHmm");
                }
            } else if (command == CommandWord.DELETE) {
                try {
                    Command deleteCommand = this.parser.parse(userInput);
                    deleteCommand.execute(this.tasks, this.ui, this.storage);
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
