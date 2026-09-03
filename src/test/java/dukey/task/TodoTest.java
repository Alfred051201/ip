package dukey.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TodoTest {

    @Test
    public void toString_newTodo_returnsTodoTypeAndUndoneStatus() {
        Todo todo = new Todo("read book");

        assertEquals("[T][ ] read book", todo.toString());
    }

    @Test
    public void toFileString_newTodo_returnsStorageFormatWithUndoneStatus() {
        Todo todo = new Todo("read book");

        assertEquals("T | 0 | read book", todo.toFileString());
    }

    @Test
    public void toFileString_doneTodo_returnsStorageFormatWithDoneStatus() {
        Todo todo = new Todo("read book");
        todo.markAsDone();

        assertEquals("T | 1 | read book", todo.toFileString());
    }
}
