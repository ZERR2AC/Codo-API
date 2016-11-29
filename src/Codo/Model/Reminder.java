package Codo.Model;

import Codo.Util.CONSTANT;
import Codo.Util.Database;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by terrychan on 23/11/2016.
 */
public class Reminder {
    public String title, content, due, remark, last_update;
    public int id, priority, type, creater_id, state;

    public Reminder(String title, String content, String due, int id, int priority, int type, int creater_id) {
        this.title = title;
        this.content = content;
        this.due = due;
        this.id = id;
        this.priority = priority;
        this.type = type;
        this.creater_id = creater_id;
    }

    public Reminder(String title, String content, String due, int state, String remark, int id, int priority, int type, int creater_id, String last_update) {
        this.title = title;
        this.content = content;
        this.due = due;
        this.state = state;
        this.remark = remark;
        this.id = id;
        this.priority = priority;
        this.type = type;
        this.creater_id = creater_id;
        this.last_update = last_update;
    }

    public static List<Reminder> getRemindersByUserId(int userId) {
        ResultSet resultSet = Database.query(String.format("SELECT state,remark,title,content,type,due,priority,channel_id,last_update,reminder_id,creater_id FROM %s INNER JOIN %s ON %s.reminder_id = %s.id WHERE user_id='%d' ORDER BY last_update DESC;",
                CONSTANT.TABLE.USER_REMINDER, CONSTANT.TABLE.REMINDER, CONSTANT.TABLE.USER_REMINDER, CONSTANT.TABLE.REMINDER, userId));
        List<Reminder> reminders = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("reminder_id");
                int state = resultSet.getInt("state");
                String remark = resultSet.getString("remark");
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                String due = resultSet.getString("due");
                String lastUpdate = resultSet.getString("last_update");
                int priority = resultSet.getInt("priority");
                int createrId = resultSet.getInt("creater_id");
                switch (resultSet.getInt("type")) {
                    case CONSTANT.REMINDER.PUBLIC:
                        int channelId = resultSet.getInt("channel_id");
                        reminders.add(new PublicReminder(title, content, due, id, priority, channelId, createrId, state, remark, lastUpdate));
                        break;
                    case CONSTANT.REMINDER.PRIVATE:
                        reminders.add(new PrivateReminder(title, content, due, id, priority, createrId, state, remark, lastUpdate));
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reminders;
    }

    public boolean ownReminder(int userId) {
        return this.creater_id == userId;
    }

    public static int getReminderTypeById(int reminderId) {
        ResultSet resultSet = Database.query(String.format("SELECT type FROM %s WHERE id='%d';", CONSTANT.TABLE.REMINDER, reminderId));
        try {
            if (resultSet.next()) {
                return resultSet.getInt("type");
            } else {
                return CONSTANT.STATE.ID_NOT_FOUND;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CONSTANT.STATE.ID_NOT_FOUND;
    }

    public static boolean isCreater(int reminder_id, int user_id) {
        ResultSet resultSet = Database.query(String.format("SELECT * FROM %s WHERE id='%d' AND creater_id='%d';", CONSTANT.TABLE.REMINDER, reminder_id, user_id));
        try {
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean createrDelete(int reminderId) {
        return Database.update(String.format("DELETE FROM %s WHERE id='%s';", CONSTANT.TABLE.REMINDER, reminderId));
    }

    public static boolean subscribeDelete(int reminderId, int userId) {
        return Database.update(String.format("DELETE FROM %s WHERE reminder_id='%s' AND user_id='%d';", CONSTANT.TABLE.USER_REMINDER, reminderId, userId));
    }
}
