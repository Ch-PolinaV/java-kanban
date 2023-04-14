package manager;

import adapter.HistoryManagerAdapter;
import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.KVTaskClient;

import java.io.IOException;
import java.time.LocalDateTime;

public class HttpTaskManager extends FileBackedTasksManager {
    public Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(InMemoryHistoryManager.class, new HistoryManagerAdapter(this))
            .create();
    public static final String KEY = "key";
    private final String url;
    private final String key;


    public HttpTaskManager(String url) {
        this.url = url;
        key = KEY;
    }

    public HttpTaskManager(String key, String url) {
        this.key = key;
        this.url = url;
    }

    @Override
    public void save() throws IOException, InterruptedException {
        new KVTaskClient(url).put(key, gson.toJson(this));
    }

    public HttpTaskManager load(String url, String key) throws IOException, InterruptedException {
        String json = new KVTaskClient(url).load(key);
        if (json.isEmpty()) {
            return new HttpTaskManager(url);
        }
        return gson.fromJson(json, HttpTaskManager.class);
    }
}