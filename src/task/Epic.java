package task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static manager.FileConversions.FORMATTER;

public class Epic extends Task {
    private List<Integer> subtaskId = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String title, String description, int duration, LocalDateTime startTime) {
        super(title, description, duration, startTime);
    }

    public Epic(int id, String title, String description, TaskStatus status, int duration, LocalDateTime startTime) {
        super(id, title, description, status, duration, startTime);
    }

    public Epic(int id, String title, String description, TaskStatus status
            , int duration, LocalDateTime startTime, List<Integer> subtaskId) {
        super(id, title, description, status, duration, startTime);
        this.subtaskId = subtaskId;
    }

    public List<Integer> getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId() {
        subtaskId.clear();
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
        return String.format("%d,%s,%s,%s,%s,%d,%s,%s",
                getId(),
                getType(),
                getTitle(),
                getStatus(),
                getDescription(),
                getDuration(),
                getStartTime().format(FORMATTER), "");
    }
}