package Task;

public class Task {
    private final int ID;
    String title;
    String description;
    String status;

    public Task(int ID, String title, String description, String status) {
        this.ID = ID;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Task(int ID, String title, String description) {
        this.ID = ID;
        this.title = title;
        this.description = description;
    }

    public int getID() {
        return ID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "ID=" + ID +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
