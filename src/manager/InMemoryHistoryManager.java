package manager;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> viewedTasks = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            viewedTasks.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        while (viewedTasks.size() > 10) {
            viewedTasks.remove(0);
        }
        return viewedTasks;
    }
}
