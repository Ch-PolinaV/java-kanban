import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        Task task1 = new Task("task", "1", Duration.ofMinutes(10), LocalDateTime.of(2024, 1, 1, 0, 0));
        Task task2 = new Task("task", "2", Duration.ofMinutes(30), LocalDateTime.of(2024, 1, 2, 10, 0));
        manager.addToTaskValue(task1);
        manager.addToTaskValue(task2);

        Epic epic1 = new Epic("epic", "1", Duration.ofMinutes(0), LocalDateTime.MAX);
        manager.addToEpicValue(epic1);

        Subtask subtask1 = new Subtask("subtask", "1", Duration.ofMinutes(10), LocalDateTime.of(2024, 1, 3, 0, 0), epic1.getId());
        Subtask subtask2 = new Subtask("subtask", "2", Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 1, 0, 0), epic1.getId());
        Subtask subtask3 = new Subtask("subtask", "3", Duration.ofMinutes(10), LocalDateTime.of(2024, 3, 1, 0, 0), epic1.getId());
        manager.addToSubtaskValue(subtask1);
        manager.addToSubtaskValue(subtask2);
        manager.addToSubtaskValue(subtask3);

        Epic epic2 = new Epic("epic", "2", Duration.ofMinutes(0), LocalDateTime.MAX);
        manager.addToEpicValue(epic2);

        System.out.println(manager.getPrioritizedTasks());

        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic2.getId());
        manager.getTaskById(task2.getId());
        manager.getSubtaskById(subtask3.getId());
        manager.getTaskById(task2.getId());
        manager.getEpicById(epic1.getId());

        System.out.println(manager.getHistory());

        manager.getTaskById(task1.getId());
        manager.getSubtaskById(subtask2.getId());
        manager.getSubtaskById(subtask3.getId());
        manager.getSubtaskById(subtask1.getId());
        manager.getEpicById(epic1.getId());
        manager.getSubtaskById(subtask1.getId());
        manager.getTaskById(task1.getId());
        manager.getEpicById(epic2.getId());

        System.out.println(manager.getHistory());

        manager.deleteTaskById(task2.getId());
        System.out.println(manager.getHistory());

        manager.deleteAllEpics();
        System.out.println(manager.getHistory());

        manager.deleteAllTasks();
        System.out.println(manager.getHistory());

        manager.deleteAllSubtasks();
        System.out.println(manager.getHistory());
    }
}