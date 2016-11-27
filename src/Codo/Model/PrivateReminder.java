package Codo.Model;

import Codo.Util.CONSTANT;
import Codo.Util.Database;
import Codo.Util.Timestamp;

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
}
