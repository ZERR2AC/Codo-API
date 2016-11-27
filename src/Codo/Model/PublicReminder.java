package Codo.Model;

import Codo.Util.CONSTANT;
import Codo.Util.Database;
import Codo.Util.Timestamp;

import java.sql.ResultSet;

/**
 * Created by terrychan on 27/11/2016.
 */
public class PublicReminder extends Reminder {
    private int channel_id;

    public PublicReminder(String title, String content, String due, int id, int priority, int channel_id, int creater_id) {
        super(title, content, due, id, priority, CONSTANT.REMINDER.PUBLIC, creater_id);
        this.channel_id = channel_id;
    }

    public PublicReminder(String title, String content, String due, int id, int priority, int channel_id, int creater_id, int state, String remark, String last_update) {
        super(title, content, due, state, remark, id, priority, CONSTANT.REMINDER.PUBLIC, creater_id, last_update);
        this.channel_id = channel_id;
    }

    public static PublicReminder newPublicReminder(int createrId, String title, String content, String due, int priority, int channelId) {
        int reminderId = Database.insert(String.format("INSERT INTO %s " +
                        "(title, creater_id, content, type, due, priority, channel_id, last_update) " +
                        "VALUE ('%s', '%d', '%s', '%d', '%s', '%d', '%d', '%s');",
                CONSTANT.TABLE.REMINDER, title, createrId, content, CONSTANT.REMINDER.PUBLIC, due, priority, channelId, Timestamp.getTime())
        );
        if (reminderId == CONSTANT.STATE.DATABASE_ERROR) return null;
        ResultSet resultSet = Channel.getUserIdInChannel(channelId);
        try {
            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                Database.update(String.format("INSERT INTO %s (user_id, reminder_id, state) VALUE ('%d', '%d', '%d')",
                        CONSTANT.TABLE.USER_REMINDER, userId, reminderId, CONSTANT.REMINDER.UNDO));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PublicReminder(title, content, due, reminderId, priority, channelId, createrId);
    }
}
