package Codo.Model;

import Codo.Util.CONSTANT;
import Codo.Util.Database;
import Codo.Util.Timestamp;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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

    // ORM: new object into database
    public static PrivateReminder newPrivateReminder(String title, String content, String due, int priority, int createrId) {
        int reminderId = Database.insert(String.format("INSERT INTO %s " +
                        "(title, creater_id, type, priority) " +
                        "VALUE ('%s', '%d', '%d', '%d');",
                CONSTANT.TABLE.REMINDER, title, createrId, CONSTANT.REMINDER.PRIVATE, priority)
        );
        // due and content is optional
        if (!due.isEmpty()) {
            Database.update(String.format("UPDATE %s ", CONSTANT.TABLE.REMINDER) +
                    String.format("SET due='%s' ", due) +
                    String.format("WHERE id='%d';", reminderId)
            );
        }
        if (!content.isEmpty()) {
            Database.update(String.format("UPDATE %s ", CONSTANT.TABLE.REMINDER) +
                    String.format("SET content='%s' ", content) +
                    String.format("WHERE id='%d';", reminderId)
            );
        }
        Database.update(String.format("INSERT INTO %s (user_id, reminder_id, state, remark, last_update) VALUE ('%d', '%d', '%d', '%s', '%s')",
                CONSTANT.TABLE.USER_REMINDER, createrId, reminderId, CONSTANT.REMINDER.UNDO, "", Timestamp.getTime()));
        if (reminderId == CONSTANT.STATE.DATABASE_ERROR) return null;
        return new PrivateReminder(title, content, due, reminderId, priority, createrId);
    }

    // return null if id is invalid or database error
    public static PrivateReminder getReminderById(int reminderId) {
        String title = "", content = "", due = "", remark, lastUpdate;
        int priority = -1, createrId = -1, state;
        // get information from table reminder
        ResultSet resultSet = Database.query(String.format("SELECT * FROM %s WHERE id='%d';", CONSTANT.TABLE.REMINDER, reminderId));
        try {
            if (resultSet.next()) {
                title = resultSet.getString("title");
                content = resultSet.getString("content");
                due = resultSet.getString("due");
                priority = resultSet.getInt("priority");
                createrId = resultSet.getInt("creater_id");
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // get information from table user_reminder
        resultSet = Database.query(String.format("SELECT * FROM %s WHERE reminder_id='%d' AND user_id='%d';", CONSTANT.TABLE.USER_REMINDER, reminderId, createrId));
        try {
            if (resultSet.next()) {
                state = resultSet.getInt("state");
                remark = resultSet.getString("remark");
                lastUpdate = resultSet.getString("last_update");
                return new PrivateReminder(title, content, due, reminderId, priority, createrId, state, remark, lastUpdate);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ORM: save a object into database
    public boolean save() {
        List<String> sqls = new ArrayList<>();
        sqls.add(String.format("UPDATE %s ", CONSTANT.TABLE.REMINDER) +
                String.format("SET title='%s',", title) +
                String.format("priority='%d' ", priority) +
                String.format("WHERE id='%d';", id));
        if (!due.isEmpty())
            sqls.add(String.format("UPDATE %s ", CONSTANT.TABLE.REMINDER) +
                    String.format("SET due='%s' ", due) +
                    String.format("WHERE id='%d';", id));
        if (!content.isEmpty())
            sqls.add(String.format("UPDATE %s ", CONSTANT.TABLE.REMINDER) +
                    String.format("SET content='%s' ", content) +
                    String.format("WHERE id='%d';", id));
        sqls.add(String.format("UPDATE %s ", CONSTANT.TABLE.USER_REMINDER) +
                String.format("SET remark='%s',", remark) +
                String.format("state='%d',", state) +
                String.format("last_update='%s' ", Timestamp.getTime()) +
                String.format("WHERE reminder_id='%d' AND user_id='%d';", id, creater_id));
        for (String sql : sqls) {
            if (!Database.update(sql)) return false;
        }
        return true;
    }
}
