package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Subtask extends Task {

    private final int epicId;
    private final TasksTypes type;

    public Subtask(String title, String description, Duration duration, LocalDateTime startTime, int epicId) {
        super(title, description, duration, startTime);
        this.epicId = epicId;
        type = TasksTypes.SUBTASK;
    }

    public Subtask(int id, String title, String description, TaskStatus status, Duration duration, LocalDateTime startTime, int epicId) {
        super(id, title, description, status, duration, startTime);
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
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s",
                getId(),
                type,
                getTitle(),
                getStatus(),
                getDescription(),
                getDuration().toMinutes(),
                getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy | HH:mm")),
                getEpicId());
    }
}