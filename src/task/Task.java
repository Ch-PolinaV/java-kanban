package task;

import java.time.LocalDateTime;

import static manager.FileConversions.FORMATTER;

public class Task {
    private int id;
    private String title;
    private String description;
    private TaskStatus status;
    private int duration;
    private LocalDateTime startTime;

    public Task(String title, String description, int duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
        status = TaskStatus.NEW;
    }

    public Task(int id, String title, String description, TaskStatus status, int duration, LocalDateTime startTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }

    public TasksTypes getType() {
        return TasksTypes.TASK;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d,%s,%s",
                id,
                getType(),
                title,
                status,
                description,
                duration,
                startTime.format(FORMATTER),
                "");
    }
}