package penguinbot.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import penguinbot.models.Deadline;
import penguinbot.models.Event;
import penguinbot.models.Task;
import penguinbot.models.ToDo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StorageTest {
    @TempDir
    Path tempDir;

    @Test
    void loadTasks_parsesSupportedLines() throws Exception {
        Path file = tempDir.resolve("tasks.txt");
        String data = String.join(System.lineSeparator(),
                "T | 1 | read book",
                "D | 0 | return book | by: 2024-08-01T10:00",
                "E | 0 | party | from: 2024-08-01T10:00 to: 2024-08-01T12:00");
        Files.writeString(file, data);

        Storage storage = new Storage(file.toString());
        List<Task> tasks = storage.loadTasks();

        assertEquals(3, tasks.size());
        assertInstanceOf(ToDo.class, tasks.get(0));
        assertInstanceOf(Deadline.class, tasks.get(1));
        assertInstanceOf(Event.class, tasks.get(2));
    }

    @Test
    void loadTasks_createsFileWhenMissing() throws Exception {
        Path file = tempDir.resolve("missing.txt");
        Storage storage = new Storage(file.toString());

        List<Task> tasks = storage.loadTasks();

        assertTrue(Files.exists(file));
        assertTrue(tasks.isEmpty());
    }
}
