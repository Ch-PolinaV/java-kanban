package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    void addToTaskValue(Task task);

    void addToEpicValue(Epic epic);

    void addToSubtaskValue(Subtask subtask);

    ArrayList<Task> getTaskValue();

    ArrayList<Epic> getEpicValue();

    ArrayList<Subtask> getSubtaskValue();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);

    void updateTaskValue(Task task);

    void updateEpicValue(Epic epic);

    void updateSubtaskValue(Subtask subtask);

    List<Task> getHistory();
}