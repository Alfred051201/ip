package dukey.ui;

import java.time.LocalDate;

import dukey.task.Task;
import dukey.task.TaskList;

/**
 * Handles all interactions with the user.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";

    /**
     * Shows the welcome banner and greeting.
     */
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

    /**
     * Shows the divider line.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Shows the goodbye message.
     */
    public void showBye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Shows an error message for data loading failures.
     */
    public void showLoadingError() {
        System.out.println("File not found");
    }

    /**
     * Shows a user-facing error message.
     *
     * @param message Error details to show.
     */
    public void showError(String message) {
        System.out.println(" OOPS!!! " + message);
    }

    /**
     * Shows all tasks in the task list.
     *
     * @param tasks Tasks to show.
     */
    public void showList(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 1; i <= tasks.size(); i++) {
            System.out.println(String.format("%d.%s", i, tasks.get(i)));
        }
    }

    /**
     * Shows deadlines and events that occur on the given date.
     *
     * @param tasks Tasks to search.
     * @param date Date to show tasks for.
     */
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

    /**
     * Shows the current number of tasks.
     *
     * @param tasks Tasks to count.
     */
    public void showTaskCount(TaskList tasks) {
        System.out.println(String.format("Now you have %d tasks in the list.", tasks.size()));
    }

    /**
     * Shows confirmation that a task was added.
     *
     * @param task Added task.
     * @param tasks Updated task list.
     */
    public void showTaskAdded(Task task, TaskList tasks) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        showTaskCount(tasks);
    }

    /**
     * Shows confirmation that a task was marked as done.
     *
     * @param task Marked task.
     */
    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    /**
     * Shows confirmation that a task was marked as not done.
     *
     * @param task Unmarked task.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    /**
     * Shows confirmation that a task was deleted.
     *
     * @param task Deleted task.
     * @param tasks Updated task list.
     */
    public void showTaskDeleted(Task task, TaskList tasks) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        showTaskCount(tasks);
    }
}
