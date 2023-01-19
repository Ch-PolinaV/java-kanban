import Task.Epic;
import Task.Subtask;
import Task.Task;
import manager.Manager;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        Task task1 = new Task(manager.createID(), "Задача", "№1", "NEW");
        Task task2 = new Task(manager.createID(), "Задача", "№2", "NEW");

        Epic epic1 = new Epic(manager.createID(), "Эпик", "№1");

        Subtask subtask1_1 = new Subtask(manager.createID(), "Подзадача №1",
                "Эпик №1", "NEW", epic1.getID());
        Subtask subtask1_2 = new Subtask(manager.createID(), "Подзадача №2",
                "Эпик №1", "NEW", epic1.getID());

        Epic epic2 = new Epic(manager.createID(), "Эпик", "№2");

        Subtask subtask2_1 = new Subtask(manager.createID(), "Подзадача №1",
                "Эпик №2", "NEW", epic2.getID());

        manager.addToTaskValue(task1);
        manager.addToTaskValue(task2);
        manager.addToEpicValue(epic1);
        manager.addToEpicValue(epic2);
        manager.addToSubtaskValue(subtask1_1, epic1);
        manager.addToSubtaskValue(subtask1_2, epic1);
        manager.addToSubtaskValue(subtask2_1, epic2);

        System.out.println(manager.getTaskValue());
        System.out.println(manager.getEpicValue());
        System.out.println(manager.getSubtaskValue() + "\n");

        task1.setStatus("DONE");
        subtask2_1.setStatus("IN_PROGRESS");
        subtask1_2.setStatus("DONE");

        manager.updateTaskValue(task1.getID(), task1);
        manager.updateSubtaskValue(subtask1_2.getID(), subtask1_2);
        manager.updateSubtaskValue(subtask2_1.getID(), subtask2_1);
        System.out.println(manager.getTaskValue());
        System.out.println(manager.getEpicValue());
        System.out.println(manager.getSubtaskValue() + "\n");

        subtask1_1.setStatus("DONE");

        manager.updateSubtaskValue(subtask1_1.getID(), subtask1_1);
        System.out.println(manager.getTaskValue());
        System.out.println(manager.getEpicValue());
        System.out.println(manager.getSubtaskValue() + "\n");

        manager.deleteTaskByID(task2.getID());
        manager.deleteEpicByID(epic1.getID());
        manager.deleteSubtaskByID(subtask2_1.getID());
        manager.updateEpicValue(epic2.getID(), epic2);
        System.out.println(manager.getTaskValue());
        System.out.println(manager.getEpicValue());
        System.out.println(manager.getSubtaskValue() + "\n");

        manager.deleteAllTasks();
        manager.deleteAllEpics();

        Task taskNew = new Task(manager.createID(), "NewTask", "1", "New");
        Epic epicNew = new Epic(manager.createID(), "NewEpic", "1");
        Subtask epicNewSubtask1 = new Subtask(manager.createID(), "1 subtask",
                "", "NEW", epicNew.getID());
        Subtask epicNewSubtask2 = new Subtask(manager.createID(), "2 subtask",
                "", "NEW", epicNew.getID());

        manager.addToTaskValue(taskNew);
        manager.addToEpicValue(epicNew);
        manager.addToSubtaskValue(epicNewSubtask1, epicNew);
        manager.addToSubtaskValue(epicNewSubtask2, epicNew);
        manager.deleteAllSubtasks();
        System.out.println(manager.getTaskByID(epicNew.getID()));
    }
}
