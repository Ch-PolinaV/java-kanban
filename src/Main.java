import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        Task task1 = new Task("task", "1", 40, LocalDateTime.of(2024, 1, 1, 10, 0));
        Task task2 = new Task("task", "2", 30, LocalDateTime.of(2024, 1, 1, 10, 30));
        manager.addToTaskValue(task1);
        manager.addToTaskValue(task2);

        Epic epic1 = new Epic("epic", "1", 0, LocalDateTime.MAX);
        manager.addToEpicValue(epic1);

        Subtask subtask1 = new Subtask("subtask", "1", 10, LocalDateTime.of(2024, 3, 1, 10, 5), epic1.getId());
        Subtask subtask2 = new Subtask("subtask", "2", 10, LocalDateTime.of(2024, 2, 1, 0, 0), epic1.getId());
        Subtask subtask3 = new Subtask("subtask", "3", 10, LocalDateTime.of(2024, 3, 1, 10, 0), epic1.getId());
        manager.addToSubtaskValue(subtask1);
        manager.addToSubtaskValue(subtask2);
        manager.addToSubtaskValue(subtask3);

        Epic epic2 = new Epic("epic", "2", 0, LocalDateTime.MAX);
        manager.addToEpicValue(epic2);

        System.out.println(manager.getPrioritizedTasks());

        task1 = new Task(task1.getId(), "task", "1", task1.getStatus(), 40, LocalDateTime.of(2024, 2, 1, 0, 0));
        manager.updateTaskValue(task1);

        System.out.println(manager.getPrioritizedTasks());
    }
}