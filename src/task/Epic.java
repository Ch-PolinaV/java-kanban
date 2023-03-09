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

    public List<Integer> getSubtaskId() {
        return subtaskId;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s", getId(), type, getTitle(), getStatus(), getDescription(), "");
    }
}