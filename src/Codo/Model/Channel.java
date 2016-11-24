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
    public static int getId(String name) {
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
            return new Channel(getId(name), name, CONSTANT.CHANNEL.CREATER);
        } else {
            return null;
        }
    }

    public static List<Channel> getAllChannel(int userId) {
        List<Channel> channels = new ArrayList<>();
        // TODO: 25/11/2016 等待订阅 channel 功能添加之后，这里需要使用 join
        ResultSet resultSet = Database.query(String.format("SELECT * FROM %s;", CONSTANT.TABLE.CHANNEL));
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int createrId = resultSet.getInt("creater_id");
                Channel channel = new Channel(id, name, createrId == userId ? CONSTANT.CHANNEL.CREATER : CONSTANT.CHANNEL.UNSUBSCRIBE);
                channels.add(channel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channels;
    }
}