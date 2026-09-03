package dukey.task;

import java.util.ArrayList;

/**
 * Contains and manages the chatbot's task list.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list backed by the given list of tasks.
     *
     * @param tasks Initial tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        this.tasks.add(task);
    }

    /**
     * Returns the task at the given one-based task number.
     *
     * @param taskNumber One-based task number.
     * @return Matching task.
     */
    public Task get(int taskNumber) {
        return this.tasks.get(taskNumber - 1);
    }

    /**
     * Deletes the task at the given one-based task number.
     *
     * @param taskNumber One-based task number.
     * @return Deleted task.
     */
    public Task delete(int taskNumber) {
        return this.tasks.remove(taskNumber - 1);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return Task count.
     */
    public int size() {
        return this.tasks.size();
    }

    /**
     * Checks whether the given one-based task number exists in the list.
     *
     * @param taskNumber One-based task number to check.
     * @return True if the task number points to an existing task.
     */
    public boolean isValidTaskNumber(int taskNumber) {
        return taskNumber >= 1 && taskNumber <= size();
    }
}
