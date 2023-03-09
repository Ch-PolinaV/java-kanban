package manager;

import exeptions.ManagerSaveException;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    private void save() {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

            bw.write("id,type,name,status,description,epic" + "\n");

            for (Task task : getTaskValue()) {
                bw.write(toString(task) + "\n");
            }
            for (Epic epic : getEpicValue()) {
                bw.write(toString(epic) + "\n");
            }
            for (Subtask subtask : getSubtaskValue()) {
                bw.write(toString(subtask) + "\n");
            }
            bw.write("\n" + historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения данных.");
        }
    }

    private String toString(Task task) {
        return task.toString();
    }

    private static Task fromString(String value) {
        String[] values = value.split(",");

        switch (values[1]) {
            case "TASK":
                Task task = new Task(values[2], values[4]);
                task.setId(Integer.parseInt(values[0]));
                task.setStatus(TaskStatus.valueOf(values[3]));
                return task;
            case "EPIC":
                Epic epic = new Epic(values[2], values[4]);
                epic.setId(Integer.parseInt(values[0]));
                epic.setStatus(TaskStatus.valueOf(values[3]));
                return epic;
            case "SUBTASK":
                Subtask subtask = new Subtask(values[2], values[4], Integer.parseInt(values[5]));
                subtask.setId(Integer.parseInt(values[0]));
                subtask.setStatus(TaskStatus.valueOf(values[3]));
                return subtask;
            default:
                return null;
        }
    }

    private static String historyToString(HistoryManager manager) {
        List<String> hist = new ArrayList<>();
        for (Integer id : manager.getHistory()) {
            hist.add(String.valueOf(id));
        }
        return String.join(",", hist);
    }

    public static List<Integer> historyFromString(String value) {
        if (value != null) {
            String[] stringId = value.split(",");
            List<Integer> ids = new ArrayList<>();

            for (String str : stringId) {
                ids.add(Integer.parseInt(str));
            }
            return ids;
        }
        return null;
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

                Task task = fromString(line);
                if (task != null) {
                    idMap.put(task.getId(), task);

                    if (line.isBlank()) {
                        break;
                    } else if (task instanceof Epic) {
                        manager.addToEpicValue((Epic) task);
                    } else if (task instanceof Subtask) {
                        manager.addToSubtaskValue((Subtask) task);
                    } else {
                        manager.addToTaskValue(task);
                    }
                }
            }

            String histLine = br.readLine();
            if (histLine != null) {
                for (Integer id : historyFromString(histLine)) {
                    manager.historyManager.add(idMap.get(id));
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла.");
        }
        return manager;
    }

    public static void main(String[] args) {
        File savedTasks = new File("src/resources/saved_tasks.csv");
        FileBackedTasksManager manager = new FileBackedTasksManager(savedTasks);

        Task task1 = new Task("task", "1");
        Epic epic1 = new Epic("epic", "1");
        Epic epic2 = new Epic("epic", "2");
        manager.addToTaskValue(task1);
        manager.addToEpicValue(epic1);
        manager.addToEpicValue(epic2);

        Subtask subtask1 = new Subtask("subtask", "1", epic1.getId());
        Subtask subtask2 = new Subtask("subtask", "2", epic1.getId());
        manager.addToSubtaskValue(subtask1);
        manager.addToSubtaskValue(subtask2);


        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic2.getId());
        manager.getTaskById(task1.getId());
        manager.getSubtaskById(subtask2.getId());
        manager.getTaskById(task1.getId());
        manager.getEpicById(epic1.getId());

        FileBackedTasksManager manager2 = loadFromFile(new File("src/resources/saved_tasks.csv"));

        System.out.println(manager2.getTaskById(1));
        System.out.println(manager2.getEpicValue());
        System.out.println(manager2.getSubtaskValue());
        System.out.println(manager2.getHistory());
    }

    @Override
    public void addToTaskValue(Task task) {
        super.addToTaskValue(task);
        save();
    }

    @Override
    public void addToEpicValue(Epic epic) {
        super.addToEpicValue(epic);
        save();
    }

    @Override
    public void addToSubtaskValue(Subtask subtask) {
        super.addToSubtaskValue(subtask);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void updateTaskValue(Task task) {
        super.updateTaskValue(task);
        save();
    }

    @Override
    public void updateEpicValue(Epic epic) {
        super.updateEpicValue(epic);
        save();
    }

    @Override
    public void updateSubtaskValue(Subtask subtask) {
        super.updateSubtaskValue(subtask);
        save();
    }
}