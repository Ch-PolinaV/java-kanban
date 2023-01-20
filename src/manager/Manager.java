package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Manager {
    private int id = 1;
    private final Map<Integer, Task> taskValue = new HashMap<>();
    private final Map<Integer, Epic> epicValue = new HashMap<>();
    private final Map<Integer, Subtask> subtaskValue = new HashMap<>();

    public void addToTaskValue(Task task) {
        int taskId = createId();
        task.setId(taskId);
        taskValue.put(taskId, task);
    }

    public void addToEpicValue(Epic epic) {
        int epicId = createId();
        epic.setId(epicId);
        epicValue.put(epicId, epic);
    }

    public void addToSubtaskValue(Subtask subtask) {
        int subtaskId = createId();
        subtask.setId(subtaskId);
        subtaskValue.put(subtaskId, subtask);
        epicValue.get(subtask.getEpicId()).getSubtaskId().add(subtask.getId());
        updateEpicStatus(subtask.getEpicId());
    }

    public ArrayList<Task> getTaskValue() {
        return new ArrayList<>(taskValue.values());
    }

    public ArrayList<Epic> getEpicValue() {
        return new ArrayList<>(epicValue.values());
    }

    public ArrayList<Subtask> getSubtaskValue() {
        return new ArrayList<>(subtaskValue.values());
    }

    public Task getTaskById(int id) {
        return taskValue.get(id);
    }

    public Epic getEpicById(int id) {
        return epicValue.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtaskValue.get(id);
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
            epic.getSubtaskId().clear();
            updateEpicStatus(epic.getId());
        }
    }

    public void deleteTaskById(int id) {
        taskValue.remove(id);
    }

    public void deleteEpicById(int id) {
        for (Integer subtaskId : epicValue.get(id).getSubtaskId()) {
            subtaskValue.remove(subtaskId);
        }
        epicValue.remove(id);
    }

    public void deleteSubtaskById(int id) {
        int epicId = subtaskValue.get(id).getEpicId();
        epicValue.get(epicId).getSubtaskId().clear();
        subtaskValue.remove(id);
        updateEpicStatus(epicId);
    }

    public void updateTaskValue(Task task) {
        taskValue.put(task.getId(), task);
    }

    public void updateEpicValue(Epic epic) {
        epicValue.put(epic.getId(), epic);
    }

    public void updateSubtaskValue(Subtask subtask) {
        subtaskValue.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    private int createId() {
        return id++;
    }

    private void updateEpicStatus(int epicId) {
        int statusIsNew = 0;
        int statusIsDone = 0;
        int countOfSubtask = epicValue.get(epicId).getSubtaskId().size();

        for (Integer subtaskId : epicValue.get(epicId).getSubtaskId()) {
            if (subtaskValue.get(subtaskId).getStatus().equals("NEW")) {
                statusIsNew++;
            } else if (subtaskValue.get(subtaskId).getStatus().equals("DONE")) {
                statusIsDone++;
            }
        }
        Epic epic = epicValue.get(epicId);

        if ((statusIsNew == countOfSubtask) || (countOfSubtask == 0)) {
            epic.setStatus("NEW");
        } else if (statusIsDone == countOfSubtask) {
            epic.setStatus("DONE");
        } else {
            epic.setStatus("IN_PROGRESS");
        }
    }
}
