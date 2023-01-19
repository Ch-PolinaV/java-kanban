package Task;

public class Subtask extends Task {

    private final int epicID;

    public Subtask(int ID, String title, String description, String status, int epicID) {
        super(ID, title, description, status);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicID=" + getEpicID() +
                ", subtaskID=" + getID() +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
