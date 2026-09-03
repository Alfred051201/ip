import dukey.exception.DukeyException;
import dukey.task.TaskList;

/**
 * Represents an executable user command.
 */
public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws DukeyException;

    public boolean isExit() {
        return false;
    }
}
