package task;

public class Task {
    private int id;
    private String title;
    private String description;
    private TaskStatus status;
    private final TasksTypes type;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        status = TaskStatus.NEW;
        type = TasksTypes.TASK;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public int getEpicId() {
        return 0;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s", id, type, title, status, description, "");
    }
}