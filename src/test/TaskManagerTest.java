package test;

import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    public T manager;

    abstract T createManager();

    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;

    @BeforeEach
    public void createAllTasks() {
        manager = createManager();

        task1 = new Task("task", "1", 10, LocalDateTime.of(2024, 1, 1, 0, 0));
        task2 = new Task("task", "2", 30, LocalDateTime.of(2024, 1, 2, 10, 0));
        manager.addToTaskValue(task1);
        manager.addToTaskValue(task2);
        epic1 = new Epic("Epic", "1", 0, LocalDateTime.MAX);
        epic2 = new Epic("Epic", "2", 0, LocalDateTime.MAX);
        manager.addToEpicValue(epic1);
        manager.addToEpicValue(epic2);
        subtask1 = new Subtask("subtask", "1", 10, LocalDateTime.of(2024, 1, 3, 0, 0), epic1.getId());
        subtask2 = new Subtask("subtask", "2", 10, LocalDateTime.of(2024, 2, 1, 0, 0), epic1.getId());
        subtask3 = new Subtask("subtask", "3", 10, LocalDateTime.of(2024, 3, 1, 0, 0), epic1.getId());
        manager.addToSubtaskValue(subtask1);
        manager.addToSubtaskValue(subtask2);
        manager.addToSubtaskValue(subtask3);
    }

    @Test
    public void shouldReturnListOfTasksSize2() {
        assertEquals(2, manager.getTaskValue().size());
    }

    @Test
    public void shouldReturnTrueForEmptyTasksList() {
        manager.deleteAllTasks();
        assertTrue(manager.getTaskValue().isEmpty());
    }

    @Test
    public void shouldReturnListOfEpicsSize2() {
        assertEquals(2, manager.getEpicValue().size());
    }

    @Test
    public void shouldReturnTrueForEmptyEpicsList() {
        manager.deleteAllEpics();
        assertTrue(manager.getEpicValue().isEmpty());
    }

    @Test
    public void shouldReturnListOfSubtasksSize3() {
        assertEquals(3, manager.getSubtaskValue().size());
    }

    @Test
    public void shouldReturnTrueForEmptySubtasksList() {
        manager.deleteAllSubtasks();
        assertTrue(manager.getSubtaskValue().isEmpty());
    }

    @Test
    public void shouldReturnTaskByCorrectId() {
        assertEquals(task1, manager.getTaskById(1));
    }

    @Test
    public void shouldReturnNullForEmptyTaskValue() {
        manager.deleteAllTasks();
        assertNull(manager.getTaskById(1));
    }

    @Test
    public void shouldReturnNullForIncorrectTaskId() {
        assertNull(manager.getTaskById(5));
    }

    @Test
    public void shouldReturnEpicByCorrectId() {
        assertEquals(epic1, manager.getEpicById(3));
    }

    @Test
    public void shouldReturnNullForEmptyEpicValue() {
        manager.deleteAllEpics();
        assertNull(manager.getEpicById(3));
    }

    @Test
    public void shouldReturnNullForIncorrectEpicId() {
        assertNull(manager.getEpicById(1));
    }

    @Test
    public void shouldReturnSubtaskByCorrectId() {
        assertEquals(subtask1, manager.getSubtaskById(subtask1.getId()));
    }

    @Test
    public void shouldReturnNullForEmptySubtaskValue() {
        manager.deleteAllSubtasks();
        assertNull(manager.getSubtaskById(5));
    }

    @Test
    public void shouldReturnNullForIncorrectSubtaskId() {
        assertNull(manager.getSubtaskById(1));
    }

    @Test
    public void shouldDeleteAllTasks() {
        int sizeBeforeDeleteAllTasks = manager.getTaskValue().size();
        assertEquals(2, sizeBeforeDeleteAllTasks);
        manager.deleteAllTasks();
        assertEquals(0, manager.getTaskValue().size());
    }

    @Test
    public void shouldReturnTrueForEmptyTaskValue() {
        manager.deleteAllTasks();
        assertTrue(manager.getTaskValue().isEmpty());
    }

    @Test
    public void shouldDeleteAllEpics() {
        int sizeBeforeDeleteAllEpics = manager.getEpicValue().size();
        assertEquals(2, sizeBeforeDeleteAllEpics);
        manager.deleteAllEpics();
        assertEquals(0, manager.getEpicValue().size());
    }

    @Test
    public void shouldReturnTrueForEmptyEpicValue() {
        manager.deleteAllEpics();
        assertTrue(manager.getEpicValue().isEmpty());
    }

    @Test
    public void shouldDeleteAllSubtasks() {
        int sizeBeforeDeleteAllSubtasks = manager.getSubtaskValue().size();
        assertEquals(3, sizeBeforeDeleteAllSubtasks);
        manager.deleteAllSubtasks();
        assertEquals(0, manager.getSubtaskValue().size());
    }

    @Test
    public void shouldReturnTrueForEmptySubtaskValue() {
        manager.deleteAllSubtasks();
        assertTrue(manager.getSubtaskValue().isEmpty());
    }

    @Test
    public void shouldDeleteTaskById() {
        assertEquals(task1, manager.getTaskById(1));
        manager.deleteTaskById(1);
        assertNull(manager.getTaskById(1));
    }

    @Test
    public void shouldReturnListDoesNotContainsTaskId() {
        manager.deleteAllTasks();
        manager.deleteTaskById(1);
        assertFalse(manager.getTaskValue().contains(manager.getTaskById(1)));
    }

    @Test
    public void shouldDoesNotChangeTaskValue() {
        List<Task> beforeList = new ArrayList<>(manager.getTaskValue());
        manager.deleteTaskById(7);
        assertEquals(beforeList, manager.getTaskValue());
    }

    @Test
    public void shouldDeleteEpicById() {
        assertEquals(epic1, manager.getEpicById(3));
        manager.deleteEpicById(3);
        assertNull(manager.getEpicById(3));
    }

    @Test
    public void shouldReturnListDoesNotContainsEpicId() {
        manager.deleteAllEpics();
        manager.deleteEpicById(1);
        assertFalse(manager.getEpicValue().contains(manager.getEpicById(1)));
    }

    @Test
    public void shouldDoesNotChangeEpicValue() {
        List<Epic> beforeList = new ArrayList<>(manager.getEpicValue());
        manager.deleteEpicById(1);
        assertEquals(beforeList, manager.getEpicValue());
    }

    @Test
    public void shouldDeleteSubtaskById() {
        assertEquals(subtask1, manager.getSubtaskById(5));
        manager.deleteSubtaskById(5);
        assertNull(manager.getSubtaskById(5));
    }

    @Test
    public void shouldReturnListDoesNotContainsSubtaskId() {
        manager.deleteAllSubtasks();
        manager.deleteSubtaskById(1);
        assertFalse(manager.getSubtaskValue().contains(manager.getSubtaskById(1)));
    }

    @Test
    public void shouldDoesNotChangeSubtaskValue() {
        List<Subtask> beforeList = new ArrayList<>(manager.getSubtaskValue());
        manager.deleteSubtaskById(1);
        assertEquals(beforeList, manager.getSubtaskValue());
    }

    @Test
    public void shouldReturnTaskDurationEquals40min() {
        task1.setDuration(40);
        manager.updateTaskValue(task1);
        assertEquals(40, task1.getDuration());
    }

    @Test
    public void shouldReturnEqualValuesForTaskBeforeAndAfterChange() {
        manager.deleteAllTasks();
        Task task = manager.getTaskById(1);
        task1.setDuration(40);
        manager.updateTaskValue(task1);
        assertEquals(task, manager.getTaskById(1));
    }

    @Test
    public void shouldDoesNotUpdateTaskValueAndReturnNull() {
        manager.updateTaskValue(manager.getTaskById(7));
        assertNull(manager.getTaskById(7));
    }

    @Test
    public void shouldReturnEpicDescriptionEquals3() {
        epic1.setDescription("3");
        manager.updateEpicValue(epic1);
        assertEquals("3", epic1.getDescription());
    }

    @Test
    public void shouldReturnEqualValuesForEpicBeforeAndAfterChange() {
        manager.deleteAllEpics();
        Epic epic = manager.getEpicById(4);
        epic2.setDuration(40);
        manager.updateEpicValue(epic2);
        assertEquals(epic, manager.getEpicById(4));
    }

    @Test
    public void shouldDoesNotUpdateEpicValueAndReturnNull() {
        manager.updateEpicValue(manager.getEpicById(1));
        assertNull(manager.getEpicById(1));
    }

    @Test
    public void shouldReturnSubtaskTittleEqualsSubtask3() {
        subtask3.setTitle("Subtask3");
        manager.updateSubtaskValue(subtask3);
        assertEquals("Subtask3", subtask3.getTitle());
    }

    @Test
    public void shouldReturnEqualValuesForSubtaskBeforeAndAfterChange() {
        manager.deleteAllSubtasks();
        Subtask subtask = manager.getSubtaskById(6);
        subtask2.setDuration(40);
        manager.updateSubtaskValue(subtask2);
        assertEquals(subtask, manager.getTaskById(6));
    }

    @Test
    public void shouldDoesNotUpdateSubtaskValueAndReturnNull() {
        manager.updateSubtaskValue(manager.getSubtaskById(1));
        assertNull(manager.getSubtaskById(1));
    }

    @Test
    public void shouldReturnChangedListAfterAddingNewTask() {
        List<Task> beforeAddNewTask = manager.getPrioritizedTasks();
        assertEquals(5, beforeAddNewTask.size());
        Task task3 = new Task("task", "3", 67, LocalDateTime.of(2023, 6, 4, 10, 0));
        manager.addToTaskValue(task3);
        List<Task> afterAddNewTask = manager.getPrioritizedTasks();
        assertEquals(6, afterAddNewTask.size());
        assertNotEquals(beforeAddNewTask.get(0), afterAddNewTask.get(0));
    }

    @Test
    public void shouldCleanPrioritizedTasksList() {
        manager.deleteAllTasks();
        manager.deleteAllSubtasks();
        assertEquals(0, manager.getPrioritizedTasks().size());
    }

    @Test
    public void shouldDoesNotUpdateSubtask2() {
        Subtask oldSubtask2 = subtask2;
        subtask2 = new Subtask(subtask2.getId(), "subtask", "2", subtask2.getStatus(),
                10, LocalDateTime.of(2024, 1, 3, 0, 0), epic1.getId());
        manager.updateSubtaskValue(subtask2);
        List<Task> sortedList = new ArrayList<>(manager.getPrioritizedTasks());
        for (Task task : sortedList) {
            if (task.getId() == subtask2.getId()) {
                subtask2 = (Subtask) task;
            }
        }
        assertEquals(oldSubtask2, subtask2);
    }
}