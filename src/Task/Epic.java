package Task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtaskIDs = new ArrayList<>();

    public Epic(int ID, String title, String description) {
        super(ID, title, description);
    }

    public List<Integer> getSubtaskID() {
        return subtaskIDs;
    }

    public void setSubtaskIDs(List<Integer> subtaskIDs) {
        this.subtaskIDs = subtaskIDs;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "ID=" + getID() +
                ", subtaskIDs=" + subtaskIDs +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
