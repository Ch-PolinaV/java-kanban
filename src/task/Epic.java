package task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskId = new ArrayList<>();
    private final TasksTypes type;

    public Epic(String title, String description) {
        super(title, description);
        type = TasksTypes.EPIC;
    }

    public Epic(int id, String title, String description, TaskStatus status) {
        super(id, title, description, status);
        type = TasksTypes.EPIC;
    }

    public List<Integer> getSubtaskId() {
        return subtaskId;
    }

    @Override
    public TasksTypes getType() {
        return TasksTypes.EPIC;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s", getId(), type, getTitle(), getStatus(), getDescription(), "");
    }
}