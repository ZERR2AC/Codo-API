package Codo.Model;

/**
 * Created by terrychan on 23/11/2016.
 */
public class Reminder {
    private String title, content, due;
    private int id, priority, type, creater_id;

    public Reminder(String title, String content, String due, int id, int priority, int type, int creater_id) {
        this.title = title;
        this.content = content;
        this.due = due;
        this.id = id;
        this.priority = priority;
        this.type = type;
        this.creater_id = creater_id;
    }
}
