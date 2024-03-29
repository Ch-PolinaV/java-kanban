package manager;

import exeptions.ManagerSaveException;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public FileBackedTasksManager() {}

    public void save() throws IOException, InterruptedException {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

            bw.write("id,type,name,status,description,duration,startTime,epic" + "\n");

            for (Task task : getTaskValue()) {
                bw.write(task.toString() + "\n");
            }
            for (Epic epic : getEpicValue()) {
                bw.write(epic.toString() + "\n");
            }
            for (Subtask subtask : getSubtaskValue()) {
                bw.write(subtask.toString() + "\n");
            }
            bw.write("\n" + FileConversions.historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения данных.");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        Map<Integer, Task> idMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();

            while (br.ready()) {
                String line = br.readLine();
                if (line.isBlank()) {
                    break;
                }
                Task task = FileConversions.fromString(line);
                if (task != null) {
                    idMap.put(task.getId(), task);

                    if (line.isBlank()) {
                        break;
                    } else {
                        switch (task.getType()) {
                            case TASK:
                                manager.addToTaskValue(task);
                                break;
                            case EPIC:
                                manager.addToEpicValue((Epic) task);
                                break;
                            case SUBTASK:
                                manager.addToSubtaskValue((Subtask) task);
                                break;
                        }
                    }
                }
            }
            String histLine = br.readLine();
            if (histLine != null) {
                for (Integer id : FileConversions.historyFromString(histLine)) {
                    manager.historyManager.add(idMap.get(id));
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return manager;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        File savedTasks = new File("src/resources/saved_tasks.csv");
        FileBackedTasksManager manager = new FileBackedTasksManager(savedTasks);

        Task task1 = new Task("task", "1", 10, LocalDateTime.of(2024, 1, 1, 22, 18));
        Epic epic1 = new Epic("epic", "1", 0, LocalDateTime.MAX);
        Epic epic2 = new Epic("epic", "2", 0, LocalDateTime.MAX);
        manager.addToTaskValue(task1);
        manager.addToEpicValue(epic1);
        manager.addToEpicValue(epic2);

        Subtask subtask1 = new Subtask("subtask", "1", 10, LocalDateTime.of(2024, 2, 1, 10, 0), epic1.getId());
        Subtask subtask2 = new Subtask("subtask", "2", 10, LocalDateTime.of(2024, 2, 1, 14, 0), epic1.getId());
        manager.addToSubtaskValue(subtask1);
        manager.addToSubtaskValue(subtask2);

        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic2.getId());
        manager.getTaskById(task1.getId());
        manager.getSubtaskById(subtask2.getId());
        manager.getTaskById(task1.getId());
        manager.getEpicById(epic1.getId());
        System.out.println(task1.getEndTime());
        System.out.println(subtask1.getEndTime());
        System.out.println(manager.getPrioritizedTasks());

        FileBackedTasksManager manager2 = loadFromFile(new File("src/resources/saved_tasks.csv"));

        System.out.println(manager2.getTaskById(1));
        System.out.println(manager2.getEpicValue());
        System.out.println(manager2.getSubtaskValue());
        System.out.println(manager2.getHistory());
        System.out.println(epic1.getEndTime());
        System.out.println(manager2.getPrioritizedTasks());
    }

    @Override
    public void addToTaskValue(Task task) throws IOException, InterruptedException {
        super.addToTaskValue(task);
        save();
    }

    @Override
    public void addToEpicValue(Epic epic) throws IOException, InterruptedException {
        super.addToEpicValue(epic);
        save();
    }

    @Override
    public void addToSubtaskValue(Subtask subtask) throws IOException, InterruptedException {
        super.addToSubtaskValue(subtask);
        save();
    }

    @Override
    public Task getTaskById(int id) throws IOException, InterruptedException {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) throws IOException, InterruptedException {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) throws IOException, InterruptedException {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void deleteAllTasks() throws IOException, InterruptedException {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() throws IOException, InterruptedException {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() throws IOException, InterruptedException {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteTaskById(int id) throws IOException, InterruptedException {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) throws IOException, InterruptedException {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) throws IOException, InterruptedException {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void updateTaskValue(Task task) throws IOException, InterruptedException {
        super.updateTaskValue(task);
        save();
    }

    @Override
    public void updateEpicValue(Epic epic) throws IOException, InterruptedException {
        super.updateEpicValue(epic);
        save();
    }

    @Override
    public void updateSubtaskValue(Subtask subtask) throws IOException, InterruptedException {
        super.updateSubtaskValue(subtask);
        save();
    }
}