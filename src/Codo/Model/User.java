package Codo.Model;

import Codo.Util.CONSTANT;
import Codo.Util.Database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.util.Date;

/**
 * Created by terrychan on 23/11/2016.
 */
public class User {
    private int id;
    private String username;

    public User(int id, String username) {
        this.id = id;
        this.username = username;
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
        String hashString = username + password + CONSTANT.DATABASE.SALT;
        byte[] hashByte = hashString.getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(hashByte);
            return convertToHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    public static int getIdByName(String username) {
        ResultSet resultSet = Database.query(String.format("SELECT id FROM %s WHERE username='%s';", CONSTANT.TABLE.USER, username));
        try {
            return resultSet.next() ? resultSet.getInt("id") : CONSTANT.STATE.ID_NOT_FOUND;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CONSTANT.STATE.ID_NOT_FOUND;
    }

    /**
     * @param username username
     * @return true if the username has been used
     */
    public static boolean hasUsername(String username) {
        ResultSet resultSet = Database.query(String.format("SELECT username FROM %s WHERE username='%s';", CONSTANT.TABLE.USER, username));
        try {
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * @param username username
     * @param password password
     */
    public static boolean doRegister(String username, String password) {
        return Database.update(String.format("INSERT INTO %s (username, password) VALUES('%s', '%s');", CONSTANT.TABLE.USER, username, passwordHash(username, password)));
    }

    /**
     * @param username username
     * @param password password
     * @return true if username and password match
     */
    private static boolean checkPassword(String username, String password) {
        ResultSet resultSet = Database.query(String.format("SELECT * FROM %s WHERE username='%s' and password='%s';", CONSTANT.TABLE.USER, username, passwordHash(username, password)));
        try {
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param username username
     * @param password password
     * @return token if username and password match else a empty String
     */
    public static String createToken(String username, String password) {
        if (checkPassword(username, password)) {
            String token = tokenHash(username);
            int id = getIdByName(username);
            Database.update(String.format("INSERT INTO %s (user_id, token) VALUES('%d', '%s');", CONSTANT.TABLE.TOKEN, id, token));
            return token;
        } else {
            return "";
        }
    }

    public static int getIdByToken(String token) {
        ResultSet resultSet = Database.query(String.format("SELECT user_id FROM %s WHERE token='%s';", CONSTANT.TABLE.TOKEN, token));
        try {
            return resultSet.next() ? resultSet.getInt("user_id") : CONSTANT.STATE.ID_NOT_FOUND;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CONSTANT.STATE.ID_NOT_FOUND;
    }
}
