package test;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {
    private KVServer kvServer;
    private HttpTaskServer httpTaskServer;
    private Task task1;
    private Task task2;
    private Epic epic;
    private Subtask subtask1;
    private Subtask subtask2;
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
    private final HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    void init() throws IOException {
        httpTaskServer = new HttpTaskServer();
        kvServer = new KVServer();
        httpTaskServer.start();
        kvServer.start();

        task1 = new Task("task", "1", 10, LocalDateTime.of(2023, 10, 1, 13, 30));
        task2 = new Task("task", "2",  10, LocalDateTime.of(2023, 11, 1, 12, 30));
        epic = new Epic("epic", "1", 60, LocalDateTime.of(2023, 1, 1, 12, 30));
        subtask1 = new Subtask("Subtask", "1",  20, LocalDateTime.of(2023, 4, 17, 12, 30), 3);
        subtask2 = new Subtask("Subtask", "1",  20, LocalDateTime.of(2023, 4, 17, 12, 30), 3);
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
    }

    @Test
    public void shouldAddNewEpic() throws IOException, InterruptedException {
        HttpResponse<String> response = createRequestPOST(epic, "/epic/");

        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldAddNewSubtask() throws IOException, InterruptedException {
        HttpResponse<String> response = createRequestPOST(subtask1, "/subtask/");

        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldUpdateTask() throws IOException, InterruptedException {
        createRequestPOST(task2, "/task/");

        task2 = new Task(1, "task", "2",  TaskStatus.IN_PROGRESS, 10, LocalDateTime.of(2023, 11, 1, 12, 30));
        HttpResponse<String> response = createRequestPOST(task1, "/task/");
        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldUpdateEpic() throws IOException, InterruptedException {
        createRequestPOST(epic, "/epic/");

        epic = new Epic(1, "epic", "1",  TaskStatus.IN_PROGRESS, 10, LocalDateTime.of(2023, 9, 1, 12, 30));
        HttpResponse<String> response = createRequestPOST(task1, "/task/");
        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldUpdateSubtask() throws IOException, InterruptedException {
        createRequestPOST(epic, "/epic/");
        createRequestPOST(subtask1, "/subtask/");

        subtask1 = new Subtask(1, "subtask", "2",  TaskStatus.IN_PROGRESS, 10, LocalDateTime.of(2023, 11, 1, 12, 30), 1);
        HttpResponse<String> response = createRequestPOST(task1, "/task/");
        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldGetAllTasks() throws IOException, InterruptedException {
        createRequestPOST(task1, "/task/");
        createRequestPOST(task2, "/task/");

        HttpResponse<String> response = client.send(createRequestGET("/task/"), HttpResponse.BodyHandlers.ofString());
        List<Task> tasks = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());

        assertEquals(2, tasks.size());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldGetAllEpics() throws IOException, InterruptedException {
        createRequestPOST(epic, "/epic/");

        HttpResponse<String> response = client.send(createRequestGET("/epic/"), HttpResponse.BodyHandlers.ofString());
        List<Epic> epics = gson.fromJson(response.body(), new TypeToken<List<Epic>>() {
        }.getType());

        assertEquals(1, epics.size());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldGetAllSubtasks() throws IOException, InterruptedException {
        createRequestPOST(subtask1, "/subtask/");
        createRequestPOST(subtask2, "/subtask/");

        HttpResponse<String> response = client.send(createRequestGET("/subtask/"), HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldGetTaskById() throws IOException, InterruptedException {
        createRequestPOST(task1, "/task/");
        task1.setId(1);

        HttpResponse<String> response = client.send(createRequestGET("/task/?id=1"), HttpResponse.BodyHandlers.ofString());
        Task task = gson.fromJson(response.body(), Task.class);

        assertEquals(200, response.statusCode());
        assertEquals(task1.getId(), task.getId());
        assertEquals(task1.getTitle(), task.getTitle());
        assertEquals(task1.getDescription(), task.getDescription());
        assertEquals(task1.getStatus(), task.getStatus());
        assertEquals(task1.getStartTime(), task.getStartTime());
        assertEquals(task1.getDuration(), task.getDuration());
    }

    @Test
    public void shouldGetEpicById() throws IOException, InterruptedException {
        createRequestPOST(epic, "/epic/");
        epic.setId(1);

        HttpResponse<String> response = client.send(createRequestGET("/epic/?id=1"), HttpResponse.BodyHandlers.ofString());
        Epic epic1 = gson.fromJson(response.body(), Epic.class);

        assertEquals(200, response.statusCode());
        assertEquals(epic.getId(), epic1.getId());
        assertEquals(epic.getTitle(), epic1.getTitle());
        assertEquals(epic.getDescription(), epic1.getDescription());
        assertEquals(epic.getStatus(), epic1.getStatus());
        assertEquals(epic.getStartTime(), epic1.getStartTime());
        assertEquals(epic.getDuration(), epic1.getDuration());
    }

    @Test
    public void shouldGetSubtaskById() throws IOException, InterruptedException {
        createRequestPOST(subtask1, "/subtask/");
        subtask1.setId(1);
        HttpResponse<String> response = client.send(createRequestGET("/subtask/?id=1"), HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldDeleteAllTasks() throws IOException, InterruptedException {

        createRequestPOST(task1, "/task/");
        createRequestPOST(task2, "/task/");

        HttpResponse<String> response = client.send(createRequestDELETE("/task/"), HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldDeleteAllEpics() throws IOException, InterruptedException {

        createRequestPOST(epic, "/epic/");

        HttpResponse<String> response = client.send(createRequestDELETE("/epic/"), HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldDeleteAllSubtasks() throws IOException, InterruptedException {

        createRequestPOST(subtask1, "/subtask/");
        createRequestPOST(subtask2, "/subtask/");

        HttpResponse<String> response = client.send(createRequestDELETE("/subtask/"), HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldDeleteTaskById() throws IOException, InterruptedException {

        createRequestPOST(task1, "/task/");

        HttpResponse<String> response = client.send(createRequestDELETE("/task/?id=1"), HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldDeleteEpicById() throws IOException, InterruptedException {

        createRequestPOST(epic, "/epic/");

        HttpResponse<String> response = client.send(createRequestDELETE("/epic/?id=1"), HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldDeleteSubtaskById() throws IOException, InterruptedException {

        createRequestPOST(subtask2, "/subtask/");

        HttpResponse<String> response = client.send(createRequestDELETE("/subtask/?id=1"), HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldGetHistory() throws IOException, InterruptedException {

        createRequestPOST(epic, "/epic/");
        createRequestPOST(subtask1, "/subtask/");
        createRequestPOST(task1, "/task/");
        createRequestPOST(task2, "/task/");
        client.send(createRequestGET("/task/?id=3"), HttpResponse.BodyHandlers.ofString());
        client.send(createRequestGET("/task/?id=4"), HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> history = client.send(createRequestGET("/history"), HttpResponse.BodyHandlers.ofString());

        assertEquals(200, history.statusCode());
    }

    @Test
    public void shouldGetPrioritizedTasks() throws IOException, InterruptedException {
        task1 = new Task(1, "task", "1", TaskStatus.NEW, 10, LocalDateTime.of(2023, 10, 1, 13, 30));
        task2 = new Task(2, "task", "2",  TaskStatus.NEW, 10, LocalDateTime.of(2023, 11, 1, 12, 30));
        createRequestPOST(task1, "/task/");
        createRequestPOST(task2, "/task/");

        HttpResponse<String> prioritized = client.send(createRequestGET("/"), HttpResponse.BodyHandlers.ofString());
        List<Task> tasks = gson.fromJson(prioritized.body(), new TypeToken<List<Task>>() {
        }.getType());

        assertEquals(2, tasks.size());
    }
    public HttpRequest createRequestGET(String path) {
        URI uri = URI.create("http://localhost:8080/tasks" + path);
        return HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Accept", "application/json")
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    public HttpResponse<String> createRequestPOST(Task task, String path) throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks" + path);
        String body = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(uri)
                .header("Accept", "application/json")
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpRequest createRequestDELETE(String path) {
        URI uri = URI.create("http://localhost:8080/tasks" + path);
        return HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .header("Accept", "application/json")
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }
}
