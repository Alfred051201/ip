import java.time.LocalDate;

/**
 * Handles all interactions with the user.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";

    public void showWelcome() {
        String banner = " ____        _              \n"
                + "|  _ \\ _   _| | _____ _   _ \n"
                + "| | | | | | | |/ / _ \\ | | |\n"
                + "| |_| | |_| |   <  __/ |_| |\n"
                + "|____/ \\__,_|_|\\_\\___|\\__, |\n"
                + "                       |___/ \n";
        showLine();
        System.out.println(banner);
        System.out.println("Hello! I'm Dukey.");
        System.out.println("What can I do for you?");
        showLine();
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public void showBye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    public void showLoadingError() {
        System.out.println("File not found");
    }

    public void showError(String message) {
        System.out.println(" OOPS!!! " + message);
    }

    public void showList(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 1; i <= tasks.size(); i++) {
            System.out.println(String.format("%d.%s", i, tasks.get(i)));
        }
    }

    public void showTasksOnDate(TaskList tasks, LocalDate date) {
        boolean hasMatchingTask = false;

        System.out.println("Here are the deadlines and events on that date:");
        for (int i = 1; i <= tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.occursOn(date)) {
                System.out.println(String.format("%d.%s", i, task));
                hasMatchingTask = true;
            }
        }

        if (!hasMatchingTask) {
            System.out.println("There are no deadlines or events on that date.");
        }
    }

    public void showTaskCount(TaskList tasks) {
        System.out.println(String.format("Now you have %d tasks in the list.", tasks.size()));
    }

    public void showTaskAdded(Task task, TaskList tasks) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        showTaskCount(tasks);
    }

    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    public void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    public void showTaskDeleted(Task task, TaskList tasks) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        showTaskCount(tasks);
    }
}
