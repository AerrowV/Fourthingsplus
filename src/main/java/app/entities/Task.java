package app.entities;

public class Task {

    private int id;
    private String name;
    private boolean done;
    private int user_id;

    public Task(int id, String name, boolean done, int user_id) {
        this.id = id;
        this.name = name;
        this.done = done;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isDone() {
        return done;
    }

    public int getUser_id() {
        return user_id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", done=" + done +
                ", user_id=" + user_id +
                '}';
    }
}
