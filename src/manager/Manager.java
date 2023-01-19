package manager;

import Task.Epic;
import Task.Subtask;
import Task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {
    int id = 1;

    private final Map<Integer, Task> taskValue = new HashMap<>();
    private final Map<Integer, Epic> epicValue = new HashMap<>();
    private final Map<Integer, Subtask> subtaskValue = new HashMap<>();

    public int createID() {
        return id++;
    }

    public void addToTaskValue(Task task) {
        taskValue.put(task.getID(), task);
    }

    public void addToEpicValue(Epic epic) {
        epicValue.put(epic.getID(), epic);
        updateEpicStatus(epic.getID());
    }

    public void addToSubtaskValue(Subtask subtask, Epic epic) {
        subtaskValue.put(subtask.getID(), subtask);
        List<Integer> subtaskIDsInEpic = new ArrayList<>(epic.getSubtaskID());
        subtaskIDsInEpic.add(subtask.getID());
        epic.setSubtaskIDs(subtaskIDsInEpic);
        updateEpicStatus(epic.getID());
    }

    private void updateEpicStatus(int epicID) {
        int statusIsNew = 0;
        int statusIsDone = 0;
        int countOfSubtask = epicValue.get(epicID).getSubtaskID().size();

        for (Integer subtaskId : epicValue.get(epicID).getSubtaskID()) {
            if (subtaskValue.get(subtaskId).getStatus().equals("NEW")) {
                statusIsNew++;
            } else if (subtaskValue.get(subtaskId).getStatus().equals("DONE")) {
                statusIsDone++;
            }
        }
        Epic epic = epicValue.get(epicID);

        if (statusIsNew == countOfSubtask) {
            epic.setStatus("NEW");
        } else if (statusIsDone == countOfSubtask) {
            epic.setStatus("DONE");
        } else {
            epic.setStatus("IN_PROGRESS");
        }
        epicValue.put(epicID, epic);
    }

    public Map<Integer, Task> getTaskValue() {
        return taskValue;
    }

    public Map<Integer, Epic> getEpicValue() {
        return epicValue;
    }

    public Map<Integer, Subtask> getSubtaskValue() {
        return subtaskValue;
    }

    public Task getTaskByID(int id) {
        Task taskById = null;
        if (taskValue.containsKey(id)) {
            taskById = taskValue.get(id);
        } else if (epicValue.containsKey(id)) {
            taskById = epicValue.get(id);
        } else if (subtaskValue.containsKey(id)) {
            taskById = subtaskValue.get(id);
        }
        return taskById;
    }

    public void deleteAllTasks() {
        taskValue.clear();
    }

    public void deleteAllEpics() {
        epicValue.clear();
        subtaskValue.clear();
    }

    public void deleteAllSubtasks() {
        subtaskValue.clear();
        for (Epic epic : epicValue.values()) {
            updateSubtaskInEpic(epic);
        }
        for (Integer epicId : epicValue.keySet()) {
            updateEpicStatus(epicId);
        }
    }

    public void updateSubtaskInEpic(Epic epic) {
        List<Integer> subtaskIDs = new ArrayList<>();
        epic.setSubtaskIDs(subtaskIDs);
    }

    public void deleteTaskByID(int id) {
        taskValue.remove(id);
    }

    public void deleteEpicByID(int id) {
        for (Integer subtaskIDs : epicValue.get(id).getSubtaskID()) {
            subtaskValue.remove(subtaskIDs);
        }
        epicValue.remove(id);
    }

    public void deleteSubtaskByID(int id) {
        updateSubtaskInEpic(epicValue.get(subtaskValue.get(id).getEpicID()));
        subtaskValue.remove(id);
    }

    public void updateTaskValue(int id, Task task) {
        taskValue.put(id, task);
    }

    public void updateEpicValue(int id, Epic epic) {
        epicValue.put(id, epic);
        updateEpicStatus(id);
    }

    public void updateSubtaskValue(int id, Subtask subtask) {
        subtaskValue.put(id, subtask);
        updateEpicStatus(subtaskValue.get(id).getEpicID());
    }
}
