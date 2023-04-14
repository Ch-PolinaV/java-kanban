package adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import manager.InMemoryHistoryManager;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HistoryManagerAdapter extends TypeAdapter<InMemoryHistoryManager> {
    TaskManager httpTaskManager;

    public HistoryManagerAdapter(TaskManager httpTaskManager) {
        this.httpTaskManager = httpTaskManager;
    }

    @Override
    public void write(JsonWriter jsonWriter, InMemoryHistoryManager historyManager) throws IOException {
        List<String> hist = new ArrayList<>();
        String history;
        for (Integer id : historyManager.getHistory()) {
            hist.add(String.valueOf(id));
        }
        history = String.join(",", hist);
        jsonWriter.value(history);
    }

    @Override
    public InMemoryHistoryManager read(JsonReader jsonReader) throws IOException {
        InMemoryHistoryManager manager = new InMemoryHistoryManager();

        String[] stringId = jsonReader.nextString().split(",");
        List<Task> tasks = httpTaskManager.getTaskValue();
        List<Epic> epics = httpTaskManager.getEpicValue();
        List<Subtask> subtasks = httpTaskManager.getSubtaskValue();

        for (String str : stringId) {
            int id = Integer.parseInt(str);
            if (tasks.get(id) != null) {
                manager.add(tasks.get(id));
            } else if (epics.get(id) != null) {
                manager.add(epics.get(id));
            } else if (subtasks.get(id) != null) {
                manager.add(subtasks.get(id));
            }
        }
        return manager;
    }

    public static List<Integer> historyFromString(String value) {
        if (value != null) {
            String[] stringId = value.split(",");
            List<Integer> ids = new ArrayList<>();

            for (String str : stringId) {
                ids.add(Integer.parseInt(str));
            }
            return ids;
        }
        return null;
    }
}