import java.io.FileNotFoundException;
import java.util.Scanner;

import dukey.exception.DukeyException;
import dukey.task.TaskList;

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

            this.ui.showLine();

            try {
                Command command = this.parser.parse(userInput);
                command.execute(this.tasks, this.ui, this.storage);
                conversation = !command.isExit();
            } catch (DukeyException e) {
                this.ui.showError(e.getMessage());
            }

            this.ui.showLine();
        }
    }

    public static void main(String[] args) {
        new Dukey("src/main/data/dukey.txt").run();
    }
}
