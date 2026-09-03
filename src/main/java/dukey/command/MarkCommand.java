package dukey.command;

import dukey.exception.DukeyException;
import dukey.storage.Storage;
import dukey.task.Task;
import dukey.task.TaskList;
import dukey.ui.Ui;

/**
 * Marks a task as done.
 */
public class MarkCommand extends Command {
    private final int taskNumber;

    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DukeyException {
        if (!tasks.isValidTaskNumber(this.taskNumber)) {
            throw new DukeyException("Please provide a valid task number.");
        }

        Task task = tasks.get(this.taskNumber);
        task.markAsDone();
        storage.save(tasks);
        ui.showTaskMarked(task);
    }
}
