package task;

public class Subtask extends Task {

    private final int epicId;
    private final TasksTypes type;

    public Subtask(String title, String description, int epicId) {
        super(title, description);
        this.epicId = epicId;
        type = TasksTypes.SUBTASK;
    }

    public Subtask(int id, String title, String description, TaskStatus status, int epicId) {
        super(id, title, description, status);
        this.epicId = epicId;
        type = TasksTypes.SUBTASK;
    }

    @Override
    public TasksTypes getType() {
        return TasksTypes.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s", getId(), type, getTitle(), getStatus(), getDescription(), getEpicId());
    }
}