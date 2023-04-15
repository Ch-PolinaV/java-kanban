package test;

import manager.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static manager.FileConversions.FORMATTER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    FileBackedTasksManagerTest() throws IOException {
    }

    @Override
    FileBackedTasksManager createManager() {
        return new FileBackedTasksManager(new File("src/resources/saved_tasks.csv"));
    }

    @BeforeEach
    public void beforeEach() throws IOException, InterruptedException {
        File savedTasks = new File("src/resources/saved_tasks.csv");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(savedTasks);

        Task task1 = new Task("task", "1", 10, LocalDateTime.of(2024, 1, 1, 22, 18));
        Epic epic1 = new Epic("epic", "1", 0, LocalDateTime.MAX);
        Epic epic2 = new Epic("epic", "2", 0, LocalDateTime.MAX);
        fileBackedTasksManager.addToTaskValue(task1);
        fileBackedTasksManager.addToEpicValue(epic1);
        fileBackedTasksManager.addToEpicValue(epic2);

        Subtask subtask1 = new Subtask("subtask", "1", 10, LocalDateTime.of(2024, 2, 1, 10, 0), epic1.getId());
        Subtask subtask2 = new Subtask("subtask", "2", 10, LocalDateTime.of(2024, 2, 1, 14, 0), epic1.getId());
        fileBackedTasksManager.addToSubtaskValue(subtask1);
        fileBackedTasksManager.addToSubtaskValue(subtask2);

        fileBackedTasksManager.getEpicById(epic1.getId());
        fileBackedTasksManager.getEpicById(epic2.getId());
        fileBackedTasksManager.getTaskById(task1.getId());
        fileBackedTasksManager.getSubtaskById(subtask2.getId());
    }

    @Test
    public void shouldLoadFromFile() {
        FileBackedTasksManager fileBackedManager = FileBackedTasksManager.loadFromFile(new File("src/resources/saved_tasks.csv"));
        assertEquals(1, fileBackedManager.getTaskValue().size());
        assertEquals(2, fileBackedManager.getEpicValue().size());
        assertEquals(2, fileBackedManager.getSubtaskValue().size());
        assertEquals(4, fileBackedManager.getHistory().size());
    }

    @Test
    public void shouldLoadFromFilePrioritizedTasks() throws IOException, InterruptedException {
        FileBackedTasksManager fileBackedManager = FileBackedTasksManager.loadFromFile(new File("src/resources/saved_tasks.csv"));
        List<Task> sortedTasks = new ArrayList<>();
        sortedTasks.add(fileBackedManager.getTaskById(1));
        sortedTasks.add(fileBackedManager.getSubtaskById(4));
        sortedTasks.add(fileBackedManager.getSubtaskById(5));
        assertEquals(sortedTasks, fileBackedManager.getPrioritizedTasks());
    }

    @Test
    public void shouldLoadFromFileWithEmptyTasks() throws IOException, InterruptedException {
        FileBackedTasksManager fileBackedManager = FileBackedTasksManager.loadFromFile(new File("src/resources/saved_tasks.csv"));
        fileBackedManager.deleteAllTasks();
        assertEquals(0, fileBackedManager.getTaskValue().size());
        assertEquals(2, fileBackedManager.getEpicValue().size());
        assertEquals(2, fileBackedManager.getSubtaskValue().size());
        assertEquals(3, fileBackedManager.getHistory().size());
    }

    @Test
    public void shouldLoadFromFileWithEmptySubtasks() throws IOException, InterruptedException {
        FileBackedTasksManager fileBackedManager = FileBackedTasksManager.loadFromFile(new File("src/resources/saved_tasks.csv"));
        fileBackedManager.deleteAllSubtasks();
        assertEquals(1, fileBackedManager.getTaskValue().size());
        assertEquals(2, fileBackedManager.getEpicValue().size());
        assertEquals(0, fileBackedManager.getSubtaskValue().size());
        assertEquals(3, fileBackedManager.getHistory().size());
    }

    @Test
    public void shouldLoadFromFileWithEmptyHistory() throws IOException, InterruptedException {
        FileBackedTasksManager fileBackedManager = FileBackedTasksManager.loadFromFile(new File("src/resources/saved_tasks.csv"));
        fileBackedManager.deleteAllTasks();
        fileBackedManager.deleteAllEpics();
        assertEquals(0, fileBackedManager.getTaskValue().size());
        assertEquals(0, fileBackedManager.getEpicValue().size());
        assertEquals(0, fileBackedManager.getSubtaskValue().size());
        assertEquals(0, fileBackedManager.getHistory().size());
    }

    @Test
    public void shouldReturnTask1StartTime() throws IOException, InterruptedException {
        FileBackedTasksManager fileBackedManager = FileBackedTasksManager.loadFromFile(new File("src/resources/saved_tasks.csv"));
        LocalDateTime taskStartTime = LocalDateTime.of(2024, 1, 1, 22, 18);
        taskStartTime.format(FORMATTER);
        assertEquals(taskStartTime, fileBackedManager.getTaskById(task1.getId()).getStartTime());
    }

    @Test
    public void shouldReturnSubtask1Duration() throws IOException, InterruptedException {
        FileBackedTasksManager fileBackedManager = FileBackedTasksManager.loadFromFile(new File("src/resources/saved_tasks.csv"));
        assertEquals(10, fileBackedManager.getSubtaskById(subtask1.getId()).getDuration());
    }
}