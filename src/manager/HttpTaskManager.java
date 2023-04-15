package manager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import server.KVTaskClient;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.util.ArrayList;

public class HttpTaskManager extends FileBackedTasksManager {
    public Gson gson = Managers.getGson();
    private KVTaskClient client;
    private final String url;


    public HttpTaskManager(String url) throws IOException, InterruptedException {
        this.url = url;
    }

    @Override
    public void save() throws IOException, InterruptedException {
        client = new KVTaskClient(url);

        client.put("tasks", gson.toJson(new ArrayList<>(taskValue.values())));
        client.put("epics", gson.toJson(new ArrayList<>(epicValue.values())));
        client.put("subtasks", gson.toJson(new ArrayList<>(subtaskValue.values())));
        client.put("history", gson.toJson(java.util.List.copyOf(this.getHistory())));
    }

    public HttpTaskManager load() throws IOException, InterruptedException {
        client = new KVTaskClient(url);

        JsonElement jsonTasks = JsonParser.parseString(client.load("tasks"));
        JsonElement jsonEpics = JsonParser.parseString(client.load("epics"));
        JsonElement jsonSubtasks = JsonParser.parseString(client.load("subtasks"));
        JsonElement jsonHistoryList = JsonParser.parseString(client.load("history"));


        if (!jsonTasks.isJsonNull()) {
            JsonArray jsonTasksArray = jsonTasks.getAsJsonArray();
            for (JsonElement jsonTask : jsonTasksArray) {
                Task task = gson.fromJson(jsonTask, Task.class);
                this.addToTaskValue(task);
            }
        }
        if (!jsonEpics.isJsonNull()) {
            JsonArray jsonEpicsArray = jsonEpics.getAsJsonArray();
            for (JsonElement jsonEpic : jsonEpicsArray) {
                Epic epic = gson.fromJson(jsonEpic, Epic.class);
                epic.setSubtaskId();
                this.addToEpicValue(epic);
            }
        }
        if (!jsonSubtasks.isJsonNull()) {
            JsonArray jsonSubtasksArray = jsonSubtasks.getAsJsonArray();
            for (JsonElement jsonSubtask : jsonSubtasksArray) {
                Subtask subtask = gson.fromJson(jsonSubtask, Subtask.class);
                this.addToSubtaskValue(subtask);
            }
        }
        if (!jsonHistoryList.isJsonNull()) {
            JsonArray jsonHistoryArray = jsonHistoryList.getAsJsonArray();
            for (JsonElement jsonId : jsonHistoryArray) {
                int id = jsonId.getAsInt();
                if (this.taskValue.containsKey(id)) {
                    this.getTaskById(id);
                } else if (this.epicValue.containsKey(id)) {
                    this.getEpicById(id);
                } else if (this.subtaskValue.containsKey(id)) {
                    this.getTaskById(id);
                }
            }
        }
        return this;
    }
}