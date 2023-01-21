import task.Epic;
import task.Subtask;
import task.Task;
import manager.Manager;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        Task task1 = new Task("Эпик", "№1");
        Task task2 = new Task("Задача", "№2");
        Task task3 = new Task("", "");
        manager.addToTaskValue(task1);
        manager.addToTaskValue(task2);

        Epic epic1 = new Epic("Эпик", "№");
        manager.addToEpicValue(epic1);

        Subtask subtask1_1 = new Subtask("Подзадача №1", "Эпик №1", epic1.getId());
        Subtask subtask1_2 = new Subtask("Подзадача №2", "Эпик №1", epic1.getId());
        manager.addToSubtaskValue(subtask1_1);
        manager.addToSubtaskValue(subtask1_2);

        Epic epic2 = new Epic("Эпик", "№2");
        manager.addToEpicValue(epic2);

        Subtask subtask2_1 = new Subtask("Подзадача №1", "Эпик №2", epic2.getId());
        manager.addToSubtaskValue(subtask2_1);

        System.out.println(manager.getTaskValue());
        System.out.println(manager.getEpicValue());
        System.out.println(manager.getSubtaskValue() + "\n");

        task1.setTitle("Задача");
        epic1.setDescription("№1");
        subtask2_1.setStatus("IN_PROGRESS");
        subtask1_2.setStatus("DONE");

        manager.updateTaskValue(task1);
        manager.updateTaskValue(task3);
        manager.updateEpicValue(epic1);
        manager.updateSubtaskValue(subtask1_2);
        manager.updateSubtaskValue(subtask2_1);
        System.out.println(manager.getTaskValue());
        System.out.println(manager.getEpicValue());
        System.out.println(manager.getSubtaskValue() + "\n");

        subtask1_1.setStatus("DONE");

        manager.updateSubtaskValue(subtask1_1);
        System.out.println(manager.getTaskValue());
        System.out.println(manager.getEpicValue());
        System.out.println(manager.getSubtaskValue() + "\n");

        manager.deleteTaskById(task2.getId());
        manager.deleteEpicById(epic2.getId());
        manager.deleteSubtaskById(subtask1_1.getId());
        System.out.println(manager.getTaskById(task1.getId()));
        System.out.println(manager.getEpicById(epic1.getId()));
        System.out.println(manager.getSubtaskById(subtask1_2.getId()) + "\n");

        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubtasks();
        System.out.println(manager.getTaskById(task1.getId()));
        System.out.println(manager.getEpicById(epic1.getId()));
        System.out.println(manager.getSubtaskById(subtask1_1.getId()));
    }
}