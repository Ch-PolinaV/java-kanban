package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int id = 1;
    protected final Map<Integer, Task> taskValue = new HashMap<>();
    protected final Map<Integer, Epic> epicValue = new HashMap<>();
    protected final Map<Integer, Subtask> subtaskValue = new HashMap<>();
    protected final TreeSet<Task> sortedTasksByStartTime = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public void addToTaskValue(Task task) {
        int taskId = createId();
        task.setId(taskId);
        taskValue.put(taskId, task);
        sortedTasksByStartTime.add(task);
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
            sortedTasksByStartTime.add(subtask);
            epicValue.get(subtask.getEpicId()).getSubtaskId().add(subtask.getId());
            updateEpicStatus(subtask.getEpicId());
            setEpicDuration(subtask.getEpicId());
            setEpicStartAndEndTime(subtask.getEpicId());
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
            sortedTasksByStartTime.remove(taskValue.get(id));
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
            sortedTasksByStartTime.remove(subtaskValue.get(id));
        }
        subtaskValue.clear();
        for (Epic epic : epicValue.values()) {
            epic.getSubtaskId().clear();
            updateEpicStatus(epic.getId());
            epic.setDuration(Duration.ofMinutes(0));
            epic.setStartTime(LocalDateTime.MAX);
            epic.setEndTime(LocalDateTime.MIN);
        }
    }

    @Override
    public void deleteTaskById(int id) {
        if (taskValue.get(id) != null) {
            historyManager.remove(id);
            sortedTasksByStartTime.remove(taskValue.get(id));
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
            sortedTasksByStartTime.remove(subtaskValue.get(id));
            subtaskValue.remove(id);
            historyManager.remove(id);
            updateEpicStatus(epicId);
            if (subtaskValue.isEmpty()) {
                epicValue.get(epicId).setDuration(Duration.ofMinutes(0));
                epicValue.get(epicId).setStartTime(LocalDateTime.MAX);
                epicValue.get(epicId).setEndTime(LocalDateTime.MIN);
            } else {
                setEpicDuration(epicId);
                setEpicStartAndEndTime(epicId);
            }
        }
    }

    @Override
    public void updateTaskValue(Task task) {
        if (task != null) {
            if (taskValue.containsKey(task.getId())) {
                taskValue.put(task.getId(), task);
                sortedTasksByStartTime.add(task);
            }
        }
    }

    @Override
    public void updateEpicValue(Epic epic) {
        if (epic != null) {
            if (epicValue.containsKey(epic.getId())) {
                epicValue.put(epic.getId(), epic);
            }
        }
    }

    @Override
    public void updateSubtaskValue(Subtask subtask) {
        if (subtask != null) {
            if (subtaskValue.containsKey(subtask.getId())) {
                subtaskValue.put(subtask.getId(), subtask);
                sortedTasksByStartTime.add(subtask);
                updateEpicStatus(subtask.getEpicId());
                setEpicDuration(subtask.getEpicId());
                setEpicStartAndEndTime(subtask.getEpicId());
            }
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortedTasksByStartTime);
    }

    @Override
    public List<Integer> getHistory() {
        return historyManager.getHistory();
    }

    private int createId() {
        return id++;
    }

    private void setEpicDuration(int epicId) {
        Duration epicDuration = Duration.ofMinutes(0);
        Epic epic = epicValue.get(epicId);
        for (Integer subtaskId : epic.getSubtaskId()) {
            epicDuration = epicDuration.plus(subtaskValue.get(subtaskId).getDuration());
        }
        epic.setDuration(epicDuration);
    }

    private void setEpicStartAndEndTime(int epicId) {
        LocalDateTime epicStartTime = LocalDateTime.MAX;
        LocalDateTime epicEndTime = LocalDateTime.MIN;
        Epic epic = epicValue.get(epicId);
        for (Integer subtaskId : epic.getSubtaskId()) {
            if (subtaskValue.get(subtaskId).getStartTime().isBefore(epicStartTime)) {
                epicStartTime = subtaskValue.get(subtaskId).getStartTime();
            }
            if (subtaskValue.get(subtaskId).getEndTime().isAfter(epicEndTime)) {
                epicEndTime = subtaskValue.get(subtaskId).getEndTime();
            }
        }
        epic.setStartTime(epicStartTime);
        epic.setEndTime(epicEndTime);
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