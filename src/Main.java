import task.Epic;
import task.Subtask;
import task.Task;
import manager.Manager;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        Task task1 = new Task("Эпик", "№1");
        Task task2 = new Task("Задача", "№2");
        manager.addToTaskValue(task1);
        manager.addToTaskValue(task2);

        Epic epic1 = new Epic("Эпик", "№1");
        manager.addToEpicValue(epic1);

        Subtask subtask1_1 = new Subtask("Подзадача №1", "Эпик №1", epic1.getId());
        Subtask subtask1_2 = new Subtask("Подзадача №2", "Эпик №1", epic1.getId());
        manager.addToSubtaskValue(subtask1_1);
        manager.addToSubtaskValue(subtask1_2);
        /* Сергей, привет! В данной реализации выходит NullPointerException если сначала создать объекты epic и subtask,
        а уже потом вызвать метод manager.addToEpicValue(epic1). Это нужно учитывать? Вроде логично что нельзя создать
        подзадачу у несуществующего эпика. Но на всякий случай хотелось бы уточнить) */

        Epic epic2 = new Epic("Эпик", "№2");
        manager.addToEpicValue(epic2);

        Subtask subtask2_1 = new Subtask("Подзадача №1", "Эпик №2", epic2.getId());
        manager.addToSubtaskValue(subtask2_1);

        System.out.println(manager.getTaskValue());
        System.out.println(manager.getEpicValue());
        System.out.println(manager.getSubtaskValue() + "\n");

        task1.setTitle("Задача");
        subtask2_1.setStatus("IN_PROGRESS");
        subtask1_2.setStatus("DONE");

        manager.updateTaskValue(task1);
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
        manager.deleteEpicById(epic1.getId());
        manager.deleteSubtaskById(subtask2_1.getId());
        System.out.println(manager.getTaskById(task1.getId()));
        System.out.println(manager.getEpicValue());
        System.out.println(manager.getSubtaskValue() + "\n");

        manager.deleteAllTasks();
        manager.deleteAllEpics();

        Task taskNew = new Task("NewTask", "1");
        manager.addToTaskValue(taskNew);

        Epic epicNew = new Epic("NewEpic", "1");
        manager.addToEpicValue(epicNew);

        epicNew.setDescription("3");
        manager.updateEpicValue(epicNew);

        Subtask epicNewSubtask1 = new Subtask("1 subtask", "", epicNew.getId());
        Subtask epicNewSubtask2 = new Subtask("2 subtask", "", epicNew.getId());
        manager.addToSubtaskValue(epicNewSubtask1);
        manager.addToSubtaskValue(epicNewSubtask2);

        System.out.println(manager.getSubtaskById(epicNewSubtask1.getId()));

        manager.deleteAllSubtasks();
        System.out.println(manager.getEpicById(epicNew.getId()));
    }
}
