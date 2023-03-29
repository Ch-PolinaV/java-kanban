package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskId = new ArrayList<>();
    private final TasksTypes type;
    private LocalDateTime endTime;

    public Epic(String title, String description, Duration duration, LocalDateTime startTime) {
        super(title, description, duration, startTime);
        type = TasksTypes.EPIC;
    }

    public Epic(int id, String title, String description, TaskStatus status, Duration duration, LocalDateTime startTime) {
        super(id, title, description, status, duration, startTime);
        type = TasksTypes.EPIC;
    }

    public List<Integer> getSubtaskId() {
        return subtaskId;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public TasksTypes getType() {
        return TasksTypes.EPIC;
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
                getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy | HH:mm")), "");
    }
}