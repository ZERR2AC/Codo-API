package Codo.Model;

import Codo.Util.CONSTANT;
import Codo.Util.Database;
import Codo.Util.Timestamp;

import java.sql.ResultSet;

/**
 * Created by terrychan on 27/11/2016.
 */
public class PublicReminder extends Reminder {
    private Channel channel;

    public PublicReminder(String title, String content, String due, int id, int priority, int channel_id, int creater_id) {
        super(title, content, due, id, priority, CONSTANT.REMINDER.PUBLIC, creater_id);
        this.channel = Channel.getChannelById(channel_id);
    }

    public PublicReminder(String title, String content, String due, int id, int priority, int channel_id, int creater_id, int state, String remark, String last_update) {
        super(title, content, due, state, remark, id, priority, CONSTANT.REMINDER.PUBLIC, creater_id, last_update);
        this.channel = Channel.getChannelById(channel_id);
    }

    // ORM: new object into database
    public static PublicReminder newPublicReminder(int createrId, String title, String content, String due, int priority, int channelId) {
        int reminderId = Database.insert(String.format("INSERT INTO %s " +
                        "(title, creater_id, content, type, priority, channel_id) " +
                        "VALUE ('%s', '%d', '%s', '%d', '%d', '%d');",
                CONSTANT.TABLE.REMINDER, title, createrId, content, CONSTANT.REMINDER.PUBLIC, priority, channelId)
        );
        if (reminderId == CONSTANT.STATE.DATABASE_ERROR) return null;
        if (!due.isEmpty())
            Database.update(String.format("UPDATE %s ", CONSTANT.TABLE.REMINDER) +
                    String.format("SET due='%s' ", due) +
                    String.format("WHERE id='%d';", reminderId));
        ResultSet resultSet = Channel.getUserIdInChannel(channelId);
        try {
            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                Database.update(String.format("INSERT INTO %s (user_id, reminder_id, state, last_update, remark) VALUE ('%d', '%d', '%d', '%s', '')",
                        CONSTANT.TABLE.USER_REMINDER, userId, reminderId, CONSTANT.REMINDER.UNDO, Timestamp.getTime()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PublicReminder(title, content, due, reminderId, priority, channelId, createrId);
    }

    public static int getChannelId(int reminderId) {
        ResultSet resultSet = Database.query(String.format("SELECT channel_id FROM %s WHERE id='%d';", CONSTANT.TABLE.REMINDER, reminderId));
        try {
            resultSet.next();
            return resultSet.getInt("channel_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CONSTANT.STATE.ID_NOT_FOUND;
    }

    public static boolean update(int reminderId, String title, String content, String due, String priority) {
        int channelId = getChannelId(reminderId);

        if (!Database.update(String.format("UPDATE %s ", CONSTANT.TABLE.CHANNEL) +
                String.format("SET last_update='%s' ", Timestamp.getTime()) +
                String.format("WHERE id='%d';", channelId)))
            return false;

        if (!Database.update(String.format("UPDATE %s ", CONSTANT.TABLE.USER_REMINDER) +
                String.format("SET last_update='%s' ", Timestamp.getTime()) +
                String.format("WHERE reminder_id='%d';", reminderId)))
            return false;

        if (!title.isEmpty())
            if (!Database.update(String.format("UPDATE %s ", CONSTANT.TABLE.REMINDER) +
                    String.format("SET title='%s' ", title) +
                    String.format("WHERE id='%d';", reminderId)))
                return false;

        if (!content.isEmpty())
            if (!Database.update(String.format("UPDATE %s ", CONSTANT.TABLE.REMINDER) +
                    String.format("SET content='%s' ", content) +
                    String.format("WHERE id='%d';", reminderId)))
                return false;

        if (!due.isEmpty())
            if (!Database.update(String.format("UPDATE %s ", CONSTANT.TABLE.REMINDER) +
                    String.format("SET due='%s' ", due) +
                    String.format("WHERE id='%d';", reminderId)))
                return false;

        // TODO: 29/11/2016 Check parameter
        if (!priority.isEmpty()) {
            if (!Database.update(String.format("UPDATE %s ", CONSTANT.TABLE.REMINDER) +
                    String.format("SET priority='%s' ", priority) +
                    String.format("WHERE id='%d';", reminderId)))
                return false;
        }

        return true;
    }

    public static boolean updateRemark(int reminderId, int userId, String remark) {
        // TODO: 29/11/2016 Check parameter
        return Database.update(String.format("UPDATE %s ", CONSTANT.TABLE.USER_REMINDER) +
                String.format("SET remark='%s',", remark) +
                String.format("last_update='%s' ", Timestamp.getTime()) +
                String.format("WHERE reminder_id='%d' AND user_id='%d';", reminderId, userId));

    }

    public static boolean updateState(int reminderId, int userId, int state) {
        // TODO: 29/11/2016 Check parameter
        return Database.update(String.format("UPDATE %s ", CONSTANT.TABLE.USER_REMINDER) +
                String.format("SET state='%d',", state) +
                String.format("last_update='%s' ", Timestamp.getTime()) +
                String.format("WHERE reminder_id='%d' AND user_id='%d';", reminderId, userId));

    }
}
