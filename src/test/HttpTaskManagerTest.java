package test;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import server.KVServer;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest extends TaskManagerTest<TaskManager> {
    private final KVServer kvServer;

    public HttpTaskManagerTest() throws IOException {
        kvServer = new KVServer();
    }

    @Override
    TaskManager createManager() {
        kvServer.start();
        return Managers.getDefault("http://localhost:8078");
    }

    @AfterEach
    public void tearDown() {
        kvServer.stop();
    }

    @Test
    public void shouldLoadTaskFromService() throws IOException, InterruptedException {
        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();
        Task task = manager.getTaskById(1);
        assertEquals(task1, task, "Задачи не совпадают");
        server.stop();
    }

    @Test
    public void shouldLoadEpicFromService() throws IOException, InterruptedException {
        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();
        Epic epic = manager.getEpicById(3);
        assertEquals(epic1, epic, "Задачи не совпадают");
        server.stop();
    }

    @Test
    public void shouldLoadSubtaskFromService() throws IOException, InterruptedException {
        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();
        Subtask subtask = manager.getSubtaskById(6);
        assertEquals(subtask2, subtask, "Задачи не совпадают");
        server.stop();
    }

    @Test
    public void shouldLoadHistory() throws IOException, InterruptedException {
        manager.getTaskById(1);
        manager.getEpicById(3);
        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();
        List<Integer> history = new ArrayList<>();
        history.add(1);
        history.add(3);
        assertEquals(history, manager.getHistory(), "Истории не совпадают");
        server.stop();
    }

    @Test
    public void shouldLoadEmptyHistory() throws IOException {
        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();
        List<Integer> history = new ArrayList<>();
        assertEquals(history, manager.getHistory(), "История не пустая");
        server.stop();
    }

    @Test
    public void shouldLoadPrioritizedTasks() throws IOException {
        List<Task> expected = manager.getPrioritizedTasks();
        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();

        assertEquals(expected, manager.getPrioritizedTasks(), "Списки не совпадают");
        server.stop();
    }

    @Test
    public void shouldLoadEmptyPrioritizedTasks() throws IOException, InterruptedException {
        manager.deleteAllTasks();
        manager.deleteAllEpics();

        List<Task> expected = new ArrayList<>();
        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();

        assertEquals(expected, manager.getPrioritizedTasks(), "Списки не совпадают");
        server.stop();
    }
}