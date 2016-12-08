package pub.codo.Model;

import pub.codo.Util.CONSTANT;
import pub.codo.Util.Database;
import pub.codo.Util.Timestamp;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by terrychan on 06/12/2016.
 */
public class Channel {
    public int id, type;
    public String name, last_update;

    Channel(int id, int type, String name, String last_update) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.last_update = last_update;
    }

    public static Channel create(String name, int creatorId) {
        String timestamp = Timestamp.getTime();
        int id = Database.insert(String.format("INSERT INTO channel (name, creator_id, last_update) VALUES('%s', '%d', '%s');",
                name, creatorId, timestamp));
        if (id == CONSTANT.STATE.DATABASE_ERROR) return null;
        else {
            Database.update(String.format("INSERT INTO user_channel (user_id, channel_id) VALUES('%d', '%d');",
                    creatorId, id));
            return new Channel(id, CONSTANT.CHANNEL.CREATER, name, timestamp);
        }
    }

    public static List<Channel> getChannels(int userId, String paraType) {
        List<Channel> channels = new ArrayList<>();
        String sql = "";
        ResultSet resultSet;
        if (paraType == null) {
            // not suggest to use this function because it runs very slow..
            sql = "SELECT * FROM channel ORDER BY last_update DESC;";
            resultSet = Database.query(sql);
            try {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int createrId = resultSet.getInt("creator_id");
                    String last_update = resultSet.getString("last_update");
                    int type;
                    if (createrId == userId)
                        type = CONSTANT.CHANNEL.CREATER;
                    else if (inChannel(userId, id))
                        type = CONSTANT.CHANNEL.SUBSCRIBE;
                    else
                        type = CONSTANT.CHANNEL.UNSUBSCRIBE;
                    channels.add(new Channel(id, type, name, last_update));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            int type = Integer.parseInt(paraType);
            switch (type) {
                case CONSTANT.CHANNEL.CREATER:
                    sql = String.format("SELECT * FROM channel WHERE creator_id='%d' ORDER BY last_update DESC;",
                            userId);
                    break;
                case CONSTANT.CHANNEL.SUBSCRIBE:
                    sql = String.format("SELECT channel.id,name,last_update FROM channel INNER JOIN user_channel " +
                                    "ON channel.id=user_channel.channel_id WHERE user_id='%d' ORDER BY last_update DESC;",
                            userId);
                    break;
                case CONSTANT.CHANNEL.UNSUBSCRIBE:
                    sql = String.format("SELECT * FROM channel WHERE id NOT IN " +
                                    "(SELECT channel.id FROM channel INNER JOIN user_channel ON channel.id=user_channel.channel_id WHERE user_id='%d')" +
                                    " ORDER BY last_update DESC;",
                            userId);
                    break;
            }
            resultSet = Database.query(sql);
            try {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String last_update = resultSet.getString("last_update");
                    channels.add(new Channel(id, type, name, last_update));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return channels;
    }

    private static boolean inChannel(int channelId, int userId) {
        // not suggest to use this function because it runs very slow..
        ResultSet resultSet = Database.query(String.format("SELECT id FROM user_channel WHERE user_id='%d' AND channel_id='%d';",
                userId, channelId));
        try {
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isCreator(int channelId, int userId) {
        ResultSet resultSet = Database.query(String.format("SELECT id FROM channel WHERE id='%d' AND creator_id='%d';",
                channelId, userId));
        try {
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean joinChannel(int userId, int channelId) {
        return Database.update(String.format("INSERT INTO user_channel (user_id, channel_id) VALUE ('%d','%d');",
                userId, channelId));
    }

    public static boolean exitChannel(int userId, int channelId) {
        return Database.delete(String.format("DELETE FROM user_channel WHERE user_id='%d' AND channel_id='%d';",
                userId, channelId));
    }

    public static ResultSet getUserIdInChannel(int channelId) {
        return Database.query(String.format("SELECT user_id FROM user_channel WHERE channel_id='%d';",
                channelId));
    }

    public static void notifyUpdate(int channnelId, String timestamp) {
        Database.update(String.format("UPDATE channel SET last_update='%s' WHERE id='%d';",
                timestamp, channnelId));
    }
}
