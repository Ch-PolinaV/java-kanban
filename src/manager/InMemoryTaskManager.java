package manager;

import exeptions.TimeIntersectionException;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

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
        checkIntersectionByTimeBeforeAdd(task);
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
            checkIntersectionByTimeBeforeAdd(subtask);
            epicValue.get(subtask.getEpicId()).getSubtaskId().add(subtask.getId());
            updateEpicStatus(subtask.getEpicId());
            setEpicDurationStartTimeAndEndTime(subtask.getEpicId());
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
            epic.setDuration(0);
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
                epicValue.get(epicId).setDuration(0);
                epicValue.get(epicId).setStartTime(LocalDateTime.MAX);
                epicValue.get(epicId).setEndTime(LocalDateTime.MIN);
            } else {
                setEpicDurationStartTimeAndEndTime(epicId);
            }
        }
    }

    @Override
    public void updateTaskValue(Task task) {
        if (task != null) {
            if (taskValue.containsKey(task.getId())) {
                checkIntersectionByTimeBeforeUpdate(task);
                taskValue.put(task.getId(), task);
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
                checkIntersectionByTimeBeforeUpdate(subtask);
                subtaskValue.put(subtask.getId(), subtask);
                updateEpicStatus(subtask.getEpicId());
                setEpicDurationStartTimeAndEndTime(subtask.getEpicId());
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

    private void setEpicDurationStartTimeAndEndTime(int epicId) {
        int epicDuration = 0;
        LocalDateTime epicStartTime = LocalDateTime.MAX;
        LocalDateTime epicEndTime = LocalDateTime.MIN;
        Epic epic = epicValue.get(epicId);

        for (Integer subtaskId : epic.getSubtaskId()) {
            epicDuration = epicDuration + subtaskValue.get(subtaskId).getDuration();
            if (subtaskValue.get(subtaskId).getStartTime().isBefore(epicStartTime)) {
                epicStartTime = subtaskValue.get(subtaskId).getStartTime();
            }
            if (subtaskValue.get(subtaskId).getEndTime().isAfter(epicEndTime)) {
                epicEndTime = subtaskValue.get(subtaskId).getEndTime();
            }
        }
        epic.setDuration(epicDuration);
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

    private void checkIntersectionByTimeBeforeAdd(Task newTask) {
        try {
            if (sortedTasksByStartTime.isEmpty() || isTasksIntersectionByTime(newTask)) {
                sortedTasksByStartTime.add(newTask);
            }
        } catch (TimeIntersectionException e) {
            System.out.print("Ошибка! Невозможно добавить задачу: ");
            System.out.println(e.getMessage());
        }
    }

    private void checkIntersectionByTimeBeforeUpdate(Task newTask) {
        switch (newTask.getType()) {
            case TASK:
                Task oldTask = taskValue.get(newTask.getId());
                sortedTasksByStartTime.remove(oldTask);
                break;
            case SUBTASK:
                Subtask oldSubtask = subtaskValue.get(newTask.getId());
                sortedTasksByStartTime.remove(oldSubtask);
                break;
        }
        try {
            if (sortedTasksByStartTime.isEmpty() || isTasksIntersectionByTime(newTask)) {
                sortedTasksByStartTime.add(newTask);
            }
        } catch (TimeIntersectionException e) {
            System.out.print("Ошибка обновления ");
            System.out.println(e.getMessage());
        }
    }

    private boolean isTasksIntersectionByTime(Task newTask) {
        boolean check = true;
        LocalDateTime newStartTime = newTask.getStartTime();
        LocalDateTime newEndTime = newTask.getEndTime();

        for (Task task : sortedTasksByStartTime) {
            LocalDateTime oldStartTime = task.getStartTime();
            LocalDateTime oldEndTime = task.getEndTime();
            if (newStartTime.isBefore(oldEndTime) && newEndTime.isAfter(oldStartTime)) {
                check = false;
            }
            if (!check) {
                throw new TimeIntersectionException(newTask.getTitle() + newTask.getDescription() + "."
                        + " Пересечение по времени с задачей: " + task.getTitle() + " - id: " + task.getId());
            }
        }
        return check;
    }
}