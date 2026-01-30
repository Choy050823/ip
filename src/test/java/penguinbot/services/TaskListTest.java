package penguinbot.services;

import org.junit.jupiter.api.Test;
import penguinbot.models.ToDo;

import static org.junit.jupiter.api.Assertions.*;

class TaskListTest {

    @Test
    void addTask_increasesSize() throws Exception {
        TaskList list = new TaskList();
        list.addTask(new ToDo("read"));

        assertEquals(1, list.getTasks().size());
    }

    @Test
    void deleteTask_decreasesSize() throws Exception {
        TaskList list = new TaskList();
        list.addTask(new ToDo("read"));
        list.addTask(new ToDo("write"));

        list.deleteTask(1);

        assertEquals(1, list.getTasks().size());
    }

    @Test
    void markAndUnmark_toggleDoneStateInString() throws Exception {
        TaskList list = new TaskList();
        list.addTask(new ToDo("read"));

        list.markTask(1);
        String marked = list.getTasks().get(0).toString();

        list.unmarkTask(1);
        String unmarked = list.getTasks().get(0).toString();

        assertTrue(marked.contains("1"));
        assertTrue(unmarked.contains("0"));
    }
}
