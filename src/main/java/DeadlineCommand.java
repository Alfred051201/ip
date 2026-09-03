/**
 * Adds a deadline task to the task list.
 */
import dukey.exception.DukeyException;
import dukey.storage.Storage;
import dukey.task.Deadline;
import dukey.task.Task;
import dukey.task.TaskList;
import dukey.ui.Ui;

public class DeadlineCommand extends Command {
    private final String description;
    private final String by;

    public DeadlineCommand(String description, String by) {
        this.description = description;
        this.by = by;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DukeyException {
        Task task = new Deadline(this.description, this.by);
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks);
    }
}
