package task;

import java.time.LocalDateTime;

import static manager.FileConversions.FORMATTER;

public class Subtask extends Task {

    private final int epicId;

    public Subtask(String title, String description, int duration, LocalDateTime startTime, int epicId) {
        super(title, description, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(int id, String title, String description, TaskStatus status, int duration, LocalDateTime startTime, int epicId) {
        super(id, title, description, status, duration, startTime);
        this.epicId = epicId;
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
        return String.format("%d,%s,%s,%s,%s,%d,%s,%d",
                getId(),
                getType(),
                getTitle(),
                getStatus(),
                getDescription(),
                getDuration(),
                getStartTime().format(FORMATTER),
                getEpicId());
    }
}