import dukey.exception.DukeyException;
import dukey.task.TaskList;

/**
 * Exits the chatbot.
 */
public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DukeyException {
        ui.showBye();
        storage.save(tasks);
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
