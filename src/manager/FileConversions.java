package manager;

import task.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileConversions {

    public final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy | HH:mm");

    public static Task fromString(String value) {
        String[] values = value.split(",");
        int id = Integer.parseInt(values[0]);
        TasksTypes type = TasksTypes.valueOf(values[1]);
        String name = values[2];
        TaskStatus status = TaskStatus.valueOf(values[3]);
        String description = values[4];
        int duration = Integer.parseInt(values[5]);
        LocalDateTime startTime = LocalDateTime.parse(values[6], FORMATTER);

        switch (type) {
            case TASK:
                return new Task(id, name, description, status, duration, startTime);
            case EPIC:
                return new Epic(id, name, description, status, duration, startTime);
            case SUBTASK:
                int epicId = Integer.parseInt(values[7]);
                return new Subtask(id, name, description, status, duration, startTime, epicId);
            default:
                return null;
        }
    }

    public static String historyToString(HistoryManager manager) {
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
}