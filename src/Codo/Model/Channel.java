package Codo.Model;

import Codo.Util.CONSTANT;
import Codo.Util.Database;

import java.sql.ResultSet;

/**
 * Created by terrychan on 24/11/2016.
 */
public class Channel {
    private String id, name, type;

    public Channel(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public static String getId(String name) {
        ResultSet resultSet = Database.query(String.format("SELECT id FROM %s WHERE name='%s';", CONSTANT.TABLE.CHANNEL, name));
        try {
            return resultSet.next() ? resultSet.getString("id") : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param name channel name
     * @return channel id
     */
    public static Channel newChannel(String name, String createrId) {
        if (Database.update(String.format("INSERT INTO %s (name, creater_id) VALUES('%s', '%s');", CONSTANT.TABLE.CHANNEL, name, createrId))) {
            return new Channel(getId(name), name, CONSTANT.CHANNEL.CREATER);
        } else {
            return null;
        }
    }
}
