import java.util.ArrayList;

/**
 * Contains and manages the chatbot's task list.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void add(Task task) {
        this.tasks.add(task);
    }

    public Task get(int taskNumber) {
        return this.tasks.get(taskNumber - 1);
    }

    public Task delete(int taskNumber) {
        return this.tasks.remove(taskNumber - 1);
    }

    public int size() {
        return this.tasks.size();
    }

    public boolean isValidTaskNumber(int taskNumber) {
        return taskNumber >= 1 && taskNumber <= size();
    }
}
