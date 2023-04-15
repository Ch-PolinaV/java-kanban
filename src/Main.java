import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.HttpTaskManager;
import server.KVServer;
import server.KVTaskClient;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        new KVServer().start();
        HttpTaskManager manager = new HttpTaskManager("http://localhost:8078");
        KVTaskClient client = new KVTaskClient("http://localhost:8078");

        Task task1 = new Task("task", "1", 10, LocalDateTime.of(2024, 1, 1, 22, 18));
        Epic epic1 = new Epic("epic", "1", 0, LocalDateTime.MAX);
        Epic epic2 = new Epic("epic", "2", 0, LocalDateTime.MAX);
        manager.addToTaskValue(task1);
        manager.addToEpicValue(epic1);
        manager.addToEpicValue(epic2);
        client.put("task", gson.toJson(task1));

        client.put("epic", gson.toJson(epic1));
        client.put("epic", gson.toJson(epic2));
        Subtask subtask1 = new Subtask("subtask", "1", 10, LocalDateTime.of(2024, 2, 1, 10, 0), epic1.getId());
        Subtask subtask2 = new Subtask("subtask", "2", 10, LocalDateTime.of(2024, 2, 1, 14, 0), epic1.getId());
        client.put("sb", gson.toJson(subtask1));
        client.put("sb", gson.toJson(subtask2));
    }
}