package Codo.Model;

import Codo.Util.CONSTANT;
import Codo.Util.Database;
import Codo.Util.Timestamp;

import java.sql.ResultSet;

/**
 * Created by terrychan on 27/11/2016.
 */
public class PrivateReminder extends Reminder {
    public PrivateReminder(String title, String content, String due, int id, int priority, int createrId) {
        super(title, content, due, id, priority, CONSTANT.REMINDER.PRIVATE, createrId);
    }

    public PrivateReminder(String title, String content, String due, int id, int priority, int createrId, int state, String remark, String last_update) {
        super(title, content, due, state, remark, id, priority, CONSTANT.REMINDER.PRIVATE, createrId, last_update);
    }

    public static PrivateReminder newPrivateReminder(String title, String content, String due, int priority, int createrId) {
        int reminderId = Database.insert(String.format("INSERT INTO %s " +
                        "(title, creater_id, content, type, due, priority, last_update) " +
                        "VALUE ('%s', '%d', '%s', '%d', '%s', '%d', '%s');",
                CONSTANT.TABLE.REMINDER, title, createrId, content, CONSTANT.REMINDER.PRIVATE, due, priority, Timestamp.getTime())
        );
        Database.update(String.format("INSERT INTO %s (user_id, reminder_id, state) VALUE ('%d', '%d', '%d')",
                CONSTANT.TABLE.USER_REMINDER, createrId, reminderId, CONSTANT.REMINDER.UNDO));
        if (reminderId == CONSTANT.STATE.DATABASE_ERROR) return null;
        return new PrivateReminder(title, content, due, reminderId, priority, createrId);
    }

    public static PrivateReminder getReminderById(int reminderId) {
        String title = "", content = "", due = "", remark, lastUpdate = "";
        int priority = -1, createrId = -1, state;
        ResultSet resultSet = Database.query(String.format("SELECT * FROM %s WHERE id='%d';", CONSTANT.TABLE.REMINDER, reminderId));
        try {
            if (resultSet.next()) {
                title = resultSet.getString("title");
                content = resultSet.getString("content");
                due = resultSet.getString("due");
                lastUpdate = resultSet.getString("last_update");
                priority = resultSet.getInt("priority");
                createrId = resultSet.getInt("creater_id");
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultSet = Database.query(String.format(String.format("SELECT * FROM %s WHERE reminder_id='%d' AND user_id='%d';", CONSTANT.TABLE.USER_REMINDER, reminderId, createrId)));
        try {
            if (resultSet.next()) {
                state = resultSet.getInt("state");
                remark = resultSet.getString("remark");
                return new PrivateReminder(title, content, due, reminderId, priority, createrId, state, remark, lastUpdate);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean ownReminder(int userId) {
        return this.creater_id == userId;
    }

    public boolean save() {
        String sql1 = String.format("UPDATE %s ", CONSTANT.TABLE.REMINDER) +
                String.format("SET title='%s',", title) +
                String.format("content='%s',", content) +
                String.format("due='%s',", due) +
                String.format("priority='%d',", priority) +
                String.format("last_update='%s' ", Timestamp.getTime()) +
                String.format("WHERE id='%d';", id);
        String sql2 = String.format("UPDATE %s ", CONSTANT.TABLE.USER_REMINDER) +
                String.format("SET remark='%s',", remark) +
                String.format("state='%d' ", state) +
                String.format("WHERE reminder_id='%d' AND user_id='%d';", id, creater_id);
        System.out.print(sql1);System.out.print(sql2);
        return Database.update(sql1) && Database.update(sql2);
    }
}
