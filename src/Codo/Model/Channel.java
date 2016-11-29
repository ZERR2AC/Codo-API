package Codo.Model;

import Codo.Util.CONSTANT;
import Codo.Util.Database;
import Codo.Util.Timestamp;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by terrychan on 24/11/2016.
 */
public class Channel {
    private int id, type;
    private String name, last_upate;

    public Channel(int id, int type, String name, String last_upate) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.last_upate = last_upate;
    }

    /**
     * @param name channel name
     * @return channel id
     */
    public static Channel newChannel(String name, int createrId) {
        int id = Database.insert(String.format("INSERT INTO %s (name, creater_id, last_update) VALUES('%s', '%d', '%s');", CONSTANT.TABLE.CHANNEL, name, createrId, Timestamp.getTime()));
        if (id != CONSTANT.STATE.DATABASE_ERROR) {
            return new Channel(id, CONSTANT.CHANNEL.CREATER, name, Timestamp.getTime());
        } else {
            return null;
        }
    }

    public static List<Channel> getChannels(int userId, String paraType) {
        List<Channel> channels = new ArrayList<>();
        // TODO: 25/11/2016 等待订阅 channel 功能添加之后，这里需要使用 join
        ResultSet resultSet = Database.query(String.format("SELECT * FROM %s ORDER BY last_update DESC;", CONSTANT.TABLE.CHANNEL));
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int createrId = resultSet.getInt("creater_id");
                String last_update = resultSet.getString("last_update");
                int type;
                if (createrId == userId)
                    type = CONSTANT.CHANNEL.CREATER;
                else if (inChannel(userId, id))
                    type = CONSTANT.CHANNEL.SUBSCRIBE;
                else
                    type = CONSTANT.CHANNEL.UNSUBSCRIBE;
                if (paraType.isEmpty() || type == Integer.parseInt(paraType)) {
                    Channel channel = new Channel(id, type, name, last_update);
                    channels.add(channel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channels;
    }

    public static boolean joinChannel(int userId, int channelId) {
        return !isCreater(channelId, userId) && Database.update(String.format("INSERT INTO %s (user_id, channel_id) VALUE ('%d','%d');", CONSTANT.TABLE.USER_CHANNEL, userId, channelId));
    }

    public static boolean exitChannel(int userId, int channelId) {
        return Database.update(String.format("DELETE FROM %s WHERE user_id='%d' AND channel_id='%d';", CONSTANT.TABLE.USER_CHANNEL, userId, channelId));
    }

    public static boolean inChannel(int userId, int channelId) {
        ResultSet resultSet = Database.query(String.format("SELECT * FROM %s WHERE user_id='%d' AND channel_id='%d';", CONSTANT.TABLE.USER_CHANNEL, userId, channelId));
        try {
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ResultSet getUserIdInChannel(int channelId) {
        return Database.query(String.format("SELECT user_id FROM %s WHERE channel_id='%d';", CONSTANT.TABLE.USER_CHANNEL, channelId));
    }

    public static boolean hasChannel(int channelId) {
        ResultSet resultSet = Database.query(String.format("SELECT * FROM %s WHERE id='%d';", CONSTANT.TABLE.CHANNEL, channelId));
        try {
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isCreater(int channelId, int userId) {
        ResultSet resultSet = Database.query(String.format("SELECT creater_id FROM %s WHERE id='%d';", CONSTANT.TABLE.CHANNEL, channelId));
        try {
            resultSet.next();
            return userId == resultSet.getInt("creater_id");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Channel getChannelById(int channelId) {
        ResultSet resultSet = Database.query(String.format("SELECT * FROM %s WHERE id='%d';", CONSTANT.TABLE.CHANNEL, channelId));
        try {
            resultSet.next();
            String name = resultSet.getString("name");
            String lastUpdate = resultSet.getString("last_update");
            // for creater to use
            return new Channel(channelId, CONSTANT.CHANNEL.CREATER, name, lastUpdate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
