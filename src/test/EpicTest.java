package test;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.TaskStatus;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    private static TaskManager manager;
    private static Epic epic;
    private static Subtask subtask1;
    private static Subtask subtask2;

    @BeforeEach
    public void beforeEach() throws IOException, InterruptedException {
        manager = Managers.getDefaultTask();

        epic = new Epic("Epic", "1", 0, LocalDateTime.MAX);
        manager.addToEpicValue(epic);
        subtask1 = new Subtask("subtask", "1", 10, LocalDateTime.of(2024, 1, 3, 0, 0), epic.getId());
        subtask2 = new Subtask("subtask", "2", 10, LocalDateTime.of(2024, 2, 1, 0, 0), epic.getId());
        manager.addToSubtaskValue(subtask1);
        manager.addToSubtaskValue(subtask2);
    }

    @Test
    public void shouldReturnStatusNewForEmptySubtasks() throws IOException, InterruptedException {
        manager.deleteAllSubtasks();
        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    public void shouldReturnStatusNew() {
        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    public void shouldReturnStatusDone() throws IOException, InterruptedException {
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        manager.updateSubtaskValue(subtask1);
        manager.updateSubtaskValue(subtask2);
        assertEquals(TaskStatus.DONE, epic.getStatus());
    }

    @Test
    public void shouldReturnStatusInProgressForSubtaskWithNewAndDoneStatus() throws IOException, InterruptedException {
        subtask1.setStatus(TaskStatus.NEW);
        subtask1.setStatus(TaskStatus.DONE);
        manager.updateSubtaskValue(subtask1);
        manager.updateSubtaskValue(subtask2);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void shouldReturnStatusInProgress() throws IOException, InterruptedException {
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtaskValue(subtask1);
        manager.updateSubtaskValue(subtask2);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }
}