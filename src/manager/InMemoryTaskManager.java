package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int id = 1;
    private final Map<Integer, Task> taskValue = new HashMap<>();
    private final Map<Integer, Epic> epicValue = new HashMap<>();
    private final Map<Integer, Subtask> subtaskValue = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public void addToTaskValue(Task task) {
        int taskId = createId();
        task.setId(taskId);
        taskValue.put(taskId, task);
    }

    @Override
    public void addToEpicValue(Epic epic) {
        int epicId = createId();
        epic.setId(epicId);
        epicValue.put(epicId, epic);
    }

    @Override
    public void addToSubtaskValue(Subtask subtask) {
        if (epicValue.get(subtask.getEpicId()) != null) {
            int subtaskId = createId();
            subtask.setId(subtaskId);
            subtaskValue.put(subtaskId, subtask);
            epicValue.get(subtask.getEpicId()).getSubtaskId().add(subtask.getId());
            updateEpicStatus(subtask.getEpicId());
        }
    }

    @Override
    public ArrayList<Task> getTaskValue() {
        return new ArrayList<>(taskValue.values());
    }

    @Override
    public ArrayList<Epic> getEpicValue() {
        return new ArrayList<>(epicValue.values());
    }

    @Override
    public ArrayList<Subtask> getSubtaskValue() {
        return new ArrayList<>(subtaskValue.values());
    }

    @Override
    public Task getTaskById(int id) {
        Task task = taskValue.getOrDefault(id, null);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epicValue.getOrDefault(id, null);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtaskValue.getOrDefault(id, null);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public void deleteAllTasks() {
        for (Integer id : taskValue.keySet()) {
            historyManager.remove(id);
        }
        taskValue.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Integer id : subtaskValue.keySet()) {
            historyManager.remove(id);
        }
        for (Integer id : epicValue.keySet()) {
            historyManager.remove(id);
        }
        epicValue.clear();
        subtaskValue.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Integer id : subtaskValue.keySet()) {
            historyManager.remove(id);
        }
        subtaskValue.clear();
        for (Epic epic : epicValue.values()) {
            epic.getSubtaskId().clear();
            updateEpicStatus(epic.getId());
        }
    }

    @Override
    public void deleteTaskById(int id) {
        if (taskValue.get(id) != null) {
            historyManager.remove(id);
            taskValue.remove(id);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        if (epicValue.get(id) != null) {
            for (Integer subtaskId : epicValue.get(id).getSubtaskId()) {
                historyManager.remove(subtaskId);
                subtaskValue.remove(subtaskId);
            }
            historyManager.remove(id);
            epicValue.remove(id);
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        if (subtaskValue.get(id) != null) {
            int epicId = subtaskValue.get(id).getEpicId();
            epicValue.get(epicId).getSubtaskId().remove((Integer) id);
            subtaskValue.remove(id);
            historyManager.remove(id);
            updateEpicStatus(epicId);
        }
    }

    @Override
    public void updateTaskValue(Task task) {
        if (taskValue.containsKey(task.getId())) {
            taskValue.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpicValue(Epic epic) {
        if (epicValue.containsKey(epic.getId())) {
            epicValue.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSubtaskValue(Subtask subtask) {
        if (subtaskValue.containsKey(subtask.getId())) {
            subtaskValue.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private int createId() {
        return id++;
    }

    private void updateEpicStatus(int epicId) {
        int statusIsNew = 0;
        int statusIsDone = 0;
        int countOfSubtask = epicValue.get(epicId).getSubtaskId().size();

        for (Integer subtaskId : epicValue.get(epicId).getSubtaskId()) {
            if (subtaskValue.get(subtaskId).getStatus() == TaskStatus.NEW) {
                statusIsNew++;
            } else if (subtaskValue.get(subtaskId).getStatus() == TaskStatus.DONE) {
                statusIsDone++;
            }
        }
        Epic epic = epicValue.get(epicId);

        if ((statusIsNew == countOfSubtask) || (countOfSubtask == 0)) {
            epic.setStatus(TaskStatus.NEW);
        } else if (statusIsDone == countOfSubtask) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}