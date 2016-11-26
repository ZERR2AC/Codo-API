package Codo.Model;

import Codo.Util.CONSTANT;
import Codo.Util.Database;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by terrychan on 24/11/2016.
 */
public class Channel {
    private int id, type;
    private String name;

    public Channel(int id, String name, int type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    /**
     * @param name channel name
     * @return channel id if found else CONSTANT.STATE.ID_NOT_FOUND
     */
    private static int getIdByName(String name) {
        ResultSet resultSet = Database.query(String.format("SELECT id FROM %s WHERE name='%s';", CONSTANT.TABLE.CHANNEL, name));
        try {
            return resultSet.next() ? resultSet.getInt("id") : CONSTANT.STATE.ID_NOT_FOUND;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CONSTANT.STATE.ID_NOT_FOUND;
    }

    /**
     * @param name channel name
     * @return channel id
     */
    public static Channel newChannel(String name, int createrId) {
        if (Database.update(String.format("INSERT INTO %s (name, creater_id) VALUES('%s', '%d');", CONSTANT.TABLE.CHANNEL, name, createrId))) {
            return new Channel(getIdByName(name), name, CONSTANT.CHANNEL.CREATER);
        } else {
            return null;
        }
    }

    public static List<Channel> getChannels(int userId, String paraType) {
        List<Channel> channels = new ArrayList<>();
        // TODO: 25/11/2016 等待订阅 channel 功能添加之后，这里需要使用 join
        ResultSet resultSet = Database.query(String.format("SELECT * FROM %s;", CONSTANT.TABLE.CHANNEL));
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int createrId = resultSet.getInt("creater_id");
                int type;
                if (createrId == userId)
                    type = CONSTANT.CHANNEL.CREATER;
                else if (inChannel(userId, id))
                    type = CONSTANT.CHANNEL.SUBSCRIBE;
                else
                    type = CONSTANT.CHANNEL.UNSUBSCRIBE;
                if (paraType.isEmpty() || type == Integer.parseInt(paraType)) {
                    Channel channel = new Channel(id, name, type);
                    channels.add(channel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channels;
    }

    public static boolean joinChannel(int userId, int channelId) {
        return Database.update(String.format("INSERT INTO %s (user_id, channel_id) VALUE ('%d','%d');", CONSTANT.TABLE.USER_CHANNEL, userId, channelId));
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
}
