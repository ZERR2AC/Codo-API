package Codo.Model;

import Codo.Util.Constant;
import Codo.Util.Database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.util.Date;

/**
 * Created by terrychan on 23/11/2016.
 */
public class User {
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
        String hashString = username + date.toString() + Constant.SALT;
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
        String hashString = username + password + Constant.SALT;
        byte[] hashByte = hashString.getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(hashByte);
            return convertToHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    public static String getUserId(String username) {
        ResultSet resultSet = Database.query(String.format("SELECT id FROM %s WHERE username='%s';", Constant.TB_USER, username));
        try {
            if (resultSet.next())
                return resultSet.getString("id");
            else
                return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean hasUsername(String username) {
        ResultSet resultSet = Database.query(String.format("SELECT username FROM %s WHERE username='%s';", Constant.TB_USER, username));
        try {
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public static void doRegister(String username, String password) {
        Database.update(String.format("INSERT INTO %s (username, password) VALUES('%s', '%s');", Constant.TB_USER, username, passwordHash(username, password)));
    }

    private static boolean checkPassword(String username, String password) {
        ResultSet resultSet = Database.query(String.format("SELECT * FROM %s WHERE username='%s' and password='%s';", Constant.TB_USER, username, passwordHash(username, password)));
        try {
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getToken(String username, String password) {
        if (checkPassword(username, password)) {
            String token = tokenHash(username);
            String id = getUserId(username);
            Database.update(String.format("INSERT INTO %s (user_id, token) VALUES('%s', '%s');", Constant.TB_TOKEN, id, token));
            return token;
        } else {
            return "";
        }
    }
}