package test;

import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {
    private HistoryManager historyManager;
    private TaskManager taskManager;
    private Task task1;
    private Epic epic2;
    private Subtask subtask3;
    private Subtask subtask4;

    @BeforeEach
    public void beforeEach() {
        historyManager = Managers.getDefaultHistory();
        taskManager = Managers.getDefault();

        task1 = new Task("task", "id 1", 10, LocalDateTime.of(2024, 1, 1, 0, 0));
        taskManager.addToTaskValue(task1);
        epic2 = new Epic("Epic", "id 2", 0, LocalDateTime.MAX);
        taskManager.addToEpicValue(epic2);
        subtask3 = new Subtask("subtask", "id 3", 10, LocalDateTime.of(2024, 1, 3, 0, 0), epic2.getId());
        subtask4 = new Subtask("subtask", "id 4", 10, LocalDateTime.of(2024, 2, 1, 0, 0), epic2.getId());
        taskManager.addToSubtaskValue(subtask3);
        taskManager.addToSubtaskValue(subtask4);
    }

    @Test
    public void shouldAddTaskToEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty());
        taskManager.getEpicById(2);
        assertEquals(1, taskManager.getHistory().size());
    }

    @Test
    public void shouldRemoveRepeatedInHistory() {
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(3);
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(3);
        assertEquals(3, taskManager.getHistory().size());
    }

    @Test
    public void shouldTryToRemoveFromEmptyHistory() {
        historyManager.remove(1);
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    public void shouldReturnEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    public void shouldRemoveFromBeginning() {
        historyManager.add(task1);
        historyManager.add(epic2);
        historyManager.add(subtask3);
        historyManager.add(subtask4);

        List<Integer> history = historyManager.getHistory();
        historyManager.remove(1);
        assertEquals(history.get(1), historyManager.getHistory().get(0));
    }

    @Test
    public void shouldRemoveFromMiddle() {
        historyManager.add(task1);
        historyManager.add(epic2);
        historyManager.add(subtask3);
        historyManager.add(subtask4);

        List<Integer> history = historyManager.getHistory();
        historyManager.remove(2);
        assertEquals(history.get(2), historyManager.getHistory().get(1));
    }

    @Test
    public void shouldRemoveFromEnd() {
        historyManager.add(task1);
        historyManager.add(epic2);
        historyManager.add(subtask3);
        historyManager.add(subtask4);

        List<Integer> history = historyManager.getHistory();
        historyManager.remove(4);
        assertNotEquals(history.get(history.size() - 1), historyManager.getHistory().get(historyManager.getHistory().size() - 1));
    }
}