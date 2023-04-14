package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    void addToTaskValue(Task task) throws IOException, InterruptedException;

    void addToEpicValue(Epic epic) throws IOException, InterruptedException;

    void addToSubtaskValue(Subtask subtask) throws IOException, InterruptedException;

    ArrayList<Task> getTaskValue();

    ArrayList<Epic> getEpicValue();

    ArrayList<Subtask> getSubtaskValue();

    Task getTaskById(int id) throws IOException, InterruptedException;

    Epic getEpicById(int id) throws IOException, InterruptedException;

    Subtask getSubtaskById(int id) throws IOException, InterruptedException;

    void deleteAllTasks() throws IOException, InterruptedException;

    void deleteAllEpics() throws IOException, InterruptedException;

    void deleteAllSubtasks() throws IOException, InterruptedException;

    void deleteTaskById(int id) throws IOException, InterruptedException;

    void deleteEpicById(int id) throws IOException, InterruptedException;

    void deleteSubtaskById(int id) throws IOException, InterruptedException;

    void updateTaskValue(Task task) throws IOException, InterruptedException;

    void updateEpicValue(Epic epic) throws IOException, InterruptedException;

    void updateSubtaskValue(Subtask subtask) throws IOException, InterruptedException;

    List<Task> getPrioritizedTasks();

    List<Integer> getHistory();
}