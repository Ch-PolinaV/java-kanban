package test;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import server.KVServer;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {
    private KVServer kvServer;
    private HttpTaskServer httpTaskServer;
    private Task task1;
    private Task task2;
    private Epic epic;
    private Subtask subtask;
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
    private final HttpClient client = HttpClient.newHttpClient();
    private final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

    @BeforeEach
    void init() throws IOException, InterruptedException {
        httpTaskServer = new HttpTaskServer();
        kvServer = new KVServer();
        httpTaskServer.start();
        kvServer.start();

        task1 = new Task(1, "task", "1", TaskStatus.NEW, 10, LocalDateTime.of(2023, 10, 1, 13, 30));
        task2 = new Task(2, "task", "2", TaskStatus.NEW, 10, LocalDateTime.of(2023, 11, 1, 12, 30));
        epic = new Epic(3, "epic", "1", TaskStatus.NEW,  60, LocalDateTime.of(2023, 1, 1, 12, 30));
        subtask = new Subtask(4, "Subtask", "1", TaskStatus.NEW, 20, LocalDateTime.of(2023, 4, 17, 12, 30), 3);
    }

    @AfterEach
    void tearDown() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    @Test
    public void shouldAddNewTask() throws IOException, InterruptedException {
        HttpResponse<String> response = createRequestPOST(task1, "/task/");

        assertEquals(200, response.statusCode());
        assertEquals("Добавлена новая задача", response.body());
    }






    public HttpRequest createRequestGET(String path) {
        URI uri = URI.create("http://localhost:8080/tasks" + path);
        return HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Accept", "application/json")
                .build();
    }

    public HttpResponse<String> createRequestPOST(Task task, String path) throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks" + path);
        String body = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(uri)
                .header("Accept", "application/json")
                .build();
        return client.send(request, handler);
    }

    public HttpRequest createRequestDELETE(String path) {
        URI uri = URI.create("http://localhost:8080/tasks" + path);
        return HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .header("Accept", "application/json")
                .build();
    }
}
