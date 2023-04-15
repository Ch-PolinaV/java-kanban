package test;

import manager.HttpTaskManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    HttpTaskManagerTest() throws IOException {
    }

    @Override
    HttpTaskManager createManager() throws IOException, InterruptedException {
        return (HttpTaskManager) Managers.getDefault("http://localhost:8078");
    }

    @Test
    public void shouldLoadFromService() throws IOException, InterruptedException {
        TaskManager httpTaskManager = new HttpTaskManager("http://localhost:8078").load();

        assertEquals(2, httpTaskManager.getTaskValue().size(), "Количество задач не совпадает");
        assertEquals(2, httpTaskManager.getEpicValue().size(), "Количество задач не совпадает");
        assertEquals(3, httpTaskManager.getSubtaskValue().size(), "Количество задач не совпадает");
    }

    @Test
    public void shouldLoadHistory() throws IOException, InterruptedException {
        manager.getTaskById(1);
        manager.getTaskById(2);

        TaskManager httpTaskManager = new HttpTaskManager("http://localhost:8078").load();
        assertEquals(2, httpTaskManager.getHistory().size());
    }

    @Test
    public void shouldLoadEmptyHistory() throws IOException, InterruptedException {
        TaskManager httpTaskManager = new HttpTaskManager("http://localhost:8078").load();
        assertEquals(0, httpTaskManager.getHistory().size());
    }

    @Test
    public void shouldLoadPrioritizedTasks() throws IOException, InterruptedException {
        TaskManager httpTaskManager = new HttpTaskManager("http://localhost:8078").load();
        assertEquals(5, httpTaskManager.getPrioritizedTasks().size());
    }

    @Test
    public void shouldLoadEmptyPrioritizedTasks() throws IOException, InterruptedException {
        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubtasks();

        TaskManager httpTaskManager = new HttpTaskManager("http://localhost:8078").load();
        assertEquals(0, httpTaskManager.getHistory().size());
    }
}