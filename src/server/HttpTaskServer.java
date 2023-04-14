package server;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final TaskManager manager;
    private final Gson gson;
    private final HttpServer server;

    public HttpTaskServer(TaskManager manager) throws IOException {
        gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        this.manager = manager;
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks/task/", this::handleTask);
        server.createContext("/tasks/epic/", this::handleEpic);
        server.createContext("/tasks/subtask/", this::handleSubtask);
        server.createContext("/tasks/", this::handlePrioritizedTask);
        server.createContext("/tasks/history", this::handleHistory);
    }

    public HttpTaskServer() throws IOException {
        gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        manager = Managers.getDefault("http://localhost:8078");
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks/task/", this::handleTask);
        server.createContext("/tasks/epic/", this::handleEpic);
        server.createContext("/tasks/subtask/", this::handleSubtask);
        server.createContext("/tasks/", this::handlePrioritizedTask);
        server.createContext("/tasks/history", this::handleHistory);
    }

    private void handleTask(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().toString();
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/tasks/task/$", path)) {
                        String response = gson.toJson(manager.getTaskValue());
                        sendText(httpExchange, response);
                        break;
                    }

                    if (Pattern.matches("^/tasks/task/.id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/task/.id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(manager.getTaskById(id));
                            sendText(httpExchange, response);
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                    break;
                }

                case "POST": {
                    if (Pattern.matches("^/tasks/task/$", path)) {

                        String body = readText(httpExchange);

                        Task task = gson.fromJson(body, Task.class);
                        int id = task.getId();

                        if (manager.getTaskById(id) != null) {
                            manager.updateTaskValue(task);
                            System.out.println("Задача с id=" + id + " обновлена");
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        } else {
                            manager.addToTaskValue(task);
                            System.out.println("Добавлена новая задача");
                            httpExchange.sendResponseHeaders(200, 0);
                        }
                    }
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/tasks/task/$", path)) {
                        manager.deleteAllTasks();
                        System.out.println("Все задачи удалены");
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    }

                    if (Pattern.matches("^/tasks/task/.id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/task/.id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            manager.deleteTaskById(id);
                            System.out.println("Удалили задачу id = " + id);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                        break;
                    } else {
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                    break;
                }
                default: {
                    System.out.println("Невозможно обработать запрос - " + requestMethod);
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private void handleEpic(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().toString();
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/tasks/epic/$", path)) {
                        String response = gson.toJson(manager.getEpicValue());
                        sendText(httpExchange, response);
                        break;
                    }

                    if (Pattern.matches("^/tasks/epic/.id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/epic/.id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(manager.getEpicById(id));
                            sendText(httpExchange, response);
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                    break;
                }

                case "POST": {
                    if (Pattern.matches("^/tasks/epic/$", path)) {

                        String body = readText(httpExchange);

                        Epic epic = gson.fromJson(body, Epic.class);
                        int id = epic.getId();

                        if (manager.getEpicById(id) != null) {
                            manager.updateEpicValue(epic);
                            System.out.println("Эпик с id=" + id + " обновлен");
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        } else {
                            manager.addToEpicValue(epic);
                            System.out.println("Добавлен новый эпик");
                            httpExchange.sendResponseHeaders(200, 0);
                        }
                    }
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/tasks/epic/$", path)) {
                        manager.deleteAllEpics();
                        System.out.println("Все эпики удалены");
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    }

                    if (Pattern.matches("^/tasks/epic/.id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/epic/.id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            manager.deleteEpicById(id);
                            System.out.println("Удалили эпик id = " + id);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                        break;
                    } else {
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                    break;
                }
                default: {
                    System.out.println("Невозможно обработать запрос - " + requestMethod);
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private void handleSubtask(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().toString();
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/tasks/subtask/$", path)) {
                        String response = gson.toJson(manager.getSubtaskValue());
                        sendText(httpExchange, response);
                        break;
                    }

                    if (Pattern.matches("^/tasks/subtask/.id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/subtask/.id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(manager.getSubtaskById(id));
                            sendText(httpExchange, response);
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                    break;
                }

                case "POST": {
                    if (Pattern.matches("^/tasks/subtask/$", path)) {

                        String body = readText(httpExchange);

                        Subtask subtask = gson.fromJson(body, Subtask.class);
                        int id = subtask.getId();

                        if (manager.getSubtaskById(id) != null) {
                            manager.updateSubtaskValue(subtask);
                            System.out.println("Подзадача с id=" + id + " обновлена");
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        } else {
                            manager.addToSubtaskValue(subtask);
                            System.out.println("Добавлена новая подзадача");
                            httpExchange.sendResponseHeaders(200, 0);
                        }
                    }
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/tasks/subtask/$", path)) {
                        manager.deleteAllSubtasks();
                        System.out.println("Все подзадачи удалены");
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    }

                    if (Pattern.matches("^/tasks/subtask/.id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/subtask/.id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            manager.deleteSubtaskById(id);
                            System.out.println("Удалили подзадачу id = " + id);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                        break;
                    } else {
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                    break;
                }
                default: {
                    System.out.println("Невозможно обработать запрос - " + requestMethod);
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private void handleHistory(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().toString();
            String requestMethod = httpExchange.getRequestMethod();

            if (Pattern.matches("^/tasks/history$", path) && requestMethod.equals("GET")) {
                String response = gson.toJson(manager.getHistory());
                sendText(httpExchange, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private void handlePrioritizedTask(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().toString();
            String requestMethod = httpExchange.getRequestMethod();

            if (Pattern.matches("^/tasks/$", path) && requestMethod.equals("GET")) {
                String response = gson.toJson(manager.getPrioritizedTasks());
                sendText(httpExchange, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/tasks");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}
