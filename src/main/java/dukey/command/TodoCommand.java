/**
 * Adds a todo task to the task list.
 */
package dukey.command;

import dukey.exception.DukeyException;
import dukey.storage.Storage;
import dukey.task.Task;
import dukey.task.TaskList;
import dukey.task.Todo;
import dukey.ui.Ui;

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
