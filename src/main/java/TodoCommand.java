/**
 * Adds a todo task to the task list.
 */
public class TodoCommand extends Command {
    private final String description;

    public TodoCommand(String description) {
        this.description = description;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DukeyException {
        Task task = new Todo(this.description);
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks);
    }
}
