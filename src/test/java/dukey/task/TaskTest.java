package dukey.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class TaskTest {

    @Test
    public void toString_newTask_returnsUndoneStatusAndDescription() {
        Task task = new Task("read book");

        assertEquals("[ ] read book", task.toString());
    }

    @Test
    public void markAsDone_newTask_updatesStatusIcon() {
        Task task = new Task("read book");

        task.markAsDone();

        assertEquals("X", task.getStatusIcon());
        assertEquals("[X] read book", task.toString());
    }

    @Test
    public void markAsUndone_doneTask_updatesStatusIcon() {
        Task task = new Task("read book");
        task.markAsDone();

        task.markAsUndone();

        assertEquals(" ", task.getStatusIcon());
        assertEquals("[ ] read book", task.toString());
    }

    @Test
    public void occursOn_plainTask_returnsFalse() {
        Task task = new Task("read book");

        assertFalse(task.occursOn(LocalDate.of(2099, 12, 6)));
    }

    @Test
    public void toFileString_plainTask_returnsEmptyString() {
        Task task = new Task("read book");

        assertEquals("", task.toFileString());
    }
}
