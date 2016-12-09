package pub.codo.Model;

import pub.codo.Util.CONSTANT.STATE;
import pub.codo.Util.CONSTANT.REMINDER;
import pub.codo.Util.CONSTANT.CHANNEL;
import pub.codo.Util.Database;
import pub.codo.Util.Timestamp;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by terrychan on 07/12/2016.
 */
public class Reminder {
    public String title, content, due, remark, last_update;
    public int id, priority, type, state, creator_id;
    public Channel channel;

    public Reminder(String title, String content, String due, String last_update, int id, int priority, int type, int state, int creator_id) {
        // for get reminders api
        this.title = title;
        this.content = content;
        this.due = due;
        this.last_update = last_update;
        this.id = id;
        this.priority = priority;
        this.type = type;
        this.state = state;
        this.creator_id = creator_id;
    }

    public Reminder(String title, String content, String due, String remark, String last_update, int id, int priority, int type, int state, int creator_id, Channel channel) {
        // for get reminders api
        this.title = title;
        this.content = content;
        this.due = due;
        this.remark = remark;
        this.last_update = last_update;
        this.id = id;
        this.priority = priority;
        this.type = type;
        this.state = state;
        this.creator_id = creator_id;
        this.channel = channel;
    }

    public Reminder(String title, String last_update, int id, int priority, int type, int state, int creator_id, Channel channel) {
        // public reminder
        this.title = title;
        this.last_update = last_update;
        this.id = id;
        this.priority = priority;
        this.type = type;
        this.state = state;
        this.creator_id = creator_id;
        this.channel = channel;
    }

    public Reminder(String title, String last_update, int id, int priority, int type, int state, int creator_id) {
        // private reminder
        this.title = title;
        this.last_update = last_update;
        this.id = id;
        this.priority = priority;
        this.type = type;
        this.state = state;
        this.creator_id = creator_id;
    }

    public static List<Reminder> getRemindersByUserId(int userId) {
        ResultSet resultSet = Database.query(String.format("SELECT reminder_id,title,content,type AS reminder_type,name,due,state,remark,priority,reminder.channel_id,reminder.creator_id,user_reminder.last_update, user_channel.id AS in_channel " +
                "FROM user_reminder " +
                "INNER JOIN reminder ON user_reminder.reminder_id = reminder.id " +
                "LEFT JOIN channel ON reminder.channel_id = channel.id " +
                "LEFT JOIN user_channel ON reminder.channel_id = user_channel.channel_id AND user_reminder.user_id = user_channel.user_id " +
                "WHERE user_reminder.user_id='%d' ORDER BY last_update DESC; ", userId));
        /*
        * if channel_id is null, reminder is private
        * if inChannel is null, user is NOT in channel
        * if creator_id == user_id, user is creator of channel
        */
        List<Reminder> reminders = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int reminderId = resultSet.getInt("reminder_id");
                int state = resultSet.getInt("state");
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                String due = resultSet.getString("due");
                String lastUpdate = resultSet.getString("last_update");
                int priority = resultSet.getInt("priority");
                int creator_id = resultSet.getInt("creator_id");
                switch (resultSet.getInt("reminder_type")) {
                    case REMINDER.PUBLIC:
                        int channelId = resultSet.getInt("channel_id");
                        String channelName = resultSet.getString("name");
                        String remark = resultSet.getString("remark");
                        int type;
                        if (resultSet.getString("in_channel") == null) type = CHANNEL.UNSUBSCRIBE;
                        else if (resultSet.getInt("creator_id") == userId) type = CHANNEL.CREATER;
                        else type = CHANNEL.SUBSCRIBE;
                        Channel channel = new Channel(channelId, type, channelName, lastUpdate);
                        reminders.add(new Reminder(title, content, due, remark, lastUpdate, reminderId, priority, REMINDER.PUBLIC, state, creator_id, channel));
                        break;
                    case REMINDER.PRIVATE:
                        reminders.add(new Reminder(title, content, due, lastUpdate, reminderId, priority, REMINDER.PRIVATE, state, creator_id));
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reminders;
    }

    public static Reminder createPublicReminder(int userId, int channelId, String title, String content, String due, int priority) {
        String timestamp = Timestamp.getTime();
        int reminderId = Database.insert(String.format("INSERT INTO reminder (title, creator_id, type, priority, channel_id) " +
                        "VALUE ('%s', '%d', '%d', '%d', '%d');",
                title, userId, REMINDER.PUBLIC, priority, channelId));
        if (reminderId == STATE.DATABASE_ERROR) return null;
        Channel.notifyUpdate(channelId, timestamp);
        // find channel name here
        String channelName;
        ResultSet resultSet = Database.query(String.format("SELECT name FROM channel WHERE id='%d';", channelId));
        try {
            resultSet.next();
            channelName = resultSet.getString("name");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        // end
        Reminder reminder = new Reminder(title, timestamp, reminderId, priority, REMINDER.PUBLIC, REMINDER.UNDO, userId,
                new Channel(channelId, CHANNEL.CREATER, channelName, timestamp));
        if (due != null && !due.isEmpty() && updateReminderTable(reminderId, "due", due))
            reminder.due = due;
        if (content != null && !content.isEmpty() && updateReminderTable(reminderId, "content", content))
            reminder.content = content;
        new Thread(() -> {
            ResultSet rs = Channel.getUserIdInChannel(channelId);
            try {
                while (rs.next()) {
                    int subscriberId = rs.getInt("user_id");
                    Database.update(String.format("INSERT INTO user_reminder (user_id, reminder_id, state, last_update) VALUE ('%d', '%d', '%d', '%s')",
                            subscriberId, reminderId, REMINDER.UNDO, timestamp));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).run();
        return reminder;
    }

    public static Reminder createPrivateReminder(int userId, String title, String content, String due, int priority) {
        String timestamp = Timestamp.getTime();
        int reminderId = Database.insert(String.format("INSERT INTO reminder (title, creator_id, type, priority) " +
                        "VALUE ('%s', '%d', '%d', '%d');",
                title, userId, REMINDER.PRIVATE, priority));
        if (reminderId == STATE.DATABASE_ERROR) return null;
        if (!Database.update(String.format("INSERT INTO user_reminder (user_id, reminder_id, state, last_update) " +
                        "VALUE ('%d', '%d', '%d', '%s')",
                userId, reminderId, REMINDER.UNDO, timestamp))) return null;
        Reminder reminder = new Reminder(title, timestamp, reminderId, priority, REMINDER.PRIVATE, REMINDER.UNDO, userId);
        if (due != null && !due.isEmpty() && updateReminderTable(reminderId, "due", due))
            reminder.due = due;
        if (content != null && !content.isEmpty() && updateReminderTable(reminderId, "content", content))
            reminder.content = content;
        return reminder;
    }

    public static boolean updateReminderTable(int reminderId, String columnLabel, String value) {
        return Database.update(String.format("UPDATE reminder SET %s='%s' WHERE id='%d';",
                columnLabel, value, reminderId));
    }

    public static boolean updateUserReminderTable(int reminderId, int userId, String columnLabel, String value) {
        return Database.update(String.format("UPDATE user_reminder SET %s='%s' WHERE reminder_id='%d' AND user_id='%d';",
                columnLabel, value, reminderId, userId));
    }

    public static boolean isCreator(int reminderId, int userId) {
        ResultSet resultSet = Database.query(String.format("SELECT id FROM reminder WHERE id='%d' AND creator_id='%d';",
                reminderId, userId));
        try {
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hasReminder(int reminderId, int userId) {
        ResultSet resultSet = Database.query(String.format("SELECT id FROM user_reminder WHERE reminder_id='%d' AND user_id='%d';",
                reminderId, userId));
        try {
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean notifyUpdate(int reminderId, String timestamp) {
        return Database.update(String.format("UPDATE user_reminder SET last_update='%s' WHERE reminder_id='%d';",
                timestamp, reminderId));
    }

    public static boolean notifyUpdate(int reminderId, int userId, String timestamp) {
        return Database.update(String.format("UPDATE user_reminder SET last_update='%s' WHERE reminder_id='%d' AND user_id='%d';",
                timestamp, reminderId, userId));
    }

    public static boolean creatorDelete(int reminderId, int userId) {
        return Database.update(String.format("DELETE FROM reminder WHERE id='%s' AND creator_id='%d';",
                reminderId, userId));
    }

    public static boolean subscribeDelete(int reminderId, int userId) {
        return Database.update(String.format("DELETE FROM user_reminder WHERE reminder_id='%d' AND user_id='%d';",
                reminderId, userId));
    }
}
