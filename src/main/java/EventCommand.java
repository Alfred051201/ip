/**
 * Adds an event task to the task list.
 */
import dukey.exception.DukeyException;
import dukey.task.Event;
import dukey.task.Task;
import dukey.task.TaskList;

public class EventCommand extends Command {
    private final String description;
    private final String from;
    private final String to;

    public EventCommand(String description, String from, String to) {
        this.description = description;
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DukeyException {
        Task task = new Event(this.description, this.from, this.to);
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks);
    }
}
