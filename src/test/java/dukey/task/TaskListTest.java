package dukey.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TaskListTest {

    @Test
    public void size_emptyTaskList_returnsZero() {
        TaskList tasks = new TaskList();

        assertEquals(0, tasks.size());
    }

    @Test
    public void add_oneTask_increasesSizeAndStoresTask() {
        TaskList tasks = new TaskList();
        Task task = new Todo("read book");

        tasks.add(task);

        assertEquals(1, tasks.size());
        assertSame(task, tasks.get(1));
    }

    @Test
    public void isValidTaskNumber_emptyTaskList_returnsFalse() {
        TaskList tasks = new TaskList();

        assertFalse(tasks.isValidTaskNumber(0));
        assertFalse(tasks.isValidTaskNumber(1));
    }

    @Test
    public void isValidTaskNumber_taskListWithTwoTasks_checksBoundariesCorrectly() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));
        tasks.add(new Todo("return book"));

        assertFalse(tasks.isValidTaskNumber(0));
        assertTrue(tasks.isValidTaskNumber(1));
        assertTrue(tasks.isValidTaskNumber(2));
        assertFalse(tasks.isValidTaskNumber(3));
    }

    @Test
    public void delete_validTaskNumber_removesAndReturnsTask() {
        TaskList tasks = new TaskList();
        Task firstTask = new Todo("read book");
        Task secondTask = new Todo("return book");
        tasks.add(firstTask);
        tasks.add(secondTask);

        Task deletedTask = tasks.delete(1);

        assertSame(firstTask, deletedTask);
        assertEquals(1, tasks.size());
        assertSame(secondTask, tasks.get(1));
    }
}
