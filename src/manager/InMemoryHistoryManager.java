package manager;

import task.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> viewedTasks = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (viewedTasks.size() >= 10) {
                viewedTasks.remove(0);
            }
            viewedTasks.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return viewedTasks;
    }
}