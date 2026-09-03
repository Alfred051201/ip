import java.time.LocalDate;

import dukey.task.TaskList;

/**
 * Shows deadlines and events that occur on a specific date.
 */
public class OnCommand extends Command {
    private final LocalDate date;

    public OnCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTasksOnDate(tasks, this.date);
    }
}
