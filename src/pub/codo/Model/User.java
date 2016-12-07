package pub.codo.Model;


import pub.codo.Util.CONSTANT.STATE;
import pub.codo.Util.CONSTANT.DATABASE;
import pub.codo.Util.Database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.util.Date;

/**
 * Created by terrychan on 23/11/2016.
 */
public class User {
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    private int id;
    private String username, token;

    private User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public static User create(String username, String password) {
        int id = Database.insert(String.format("INSERT INTO user (username, password) VALUES('%s', '%s');",
                username, passwordHash(username, password)));
        if (id == STATE.DATABASE_ERROR) return null;
        else return new User(id, username);

    }

    public static User login(String username, String password) {
        ResultSet resultSet = Database.query(String.format("SELECT * FROM user WHERE username='%s' and password='%s';",
                username, passwordHash(username, password)));
        try {
            if (resultSet.next()) {
                User user = new User(resultSet.getInt("id"), resultSet.getString("username"));
                user.createToken();
                return user;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void createToken() {
        token = tokenHash(username);
        Database.update(String.format("INSERT INTO token (user_id, token) VALUES('%d', '%s');",
                id, token));
    }

    private static String convertToHexString(byte[] bytes) {
        String result = "";
        for (byte aByte : bytes) {
            String temp = Integer.toHexString(0xff & aByte);
            result += temp;
        }
        return result;
    }

    private static String tokenHash(String username) {
        Date date = new Date();
        String hashString = username + date.toString() + CONSTANT.DATABASE.SALT;
        byte[] hashByte = hashString.getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(hashByte);
            return convertToHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    private static String passwordHash(String username, String password) {
        String hashString = username + password + DATABASE.SALT;
        byte[] hashByte = hashString.getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(hashByte);
            return convertToHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    public static User getUserByToken(String token) {
        ResultSet resultSet = Database.query(String.format("SELECT user_id,token,username FROM token INNER JOIN user ON token.user_id=user.id WHERE token='%s';", token));
        try {
            if (resultSet.next()) {
                return new User(resultSet.getInt("user_id"), resultSet.getString("username"));
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
