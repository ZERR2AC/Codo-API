package Codo.Util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by terrychan on 23/11/2016.
 */
public class Database {
    private Database() {
    }

    private static Connection getConnection() {
        String connectString = "jdbc:mysql://" + Constant.DB_HOST
                + ":" + Constant.DB_PORT
                + "/" + Constant.DB_NAME
                + "?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(connectString, Constant.DB_USER, Constant.DB_PASSWORD);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResultSet query(String sql) {
        ResultSet rs = null;
        try {
            Connection conn = getConnection();
            Statement stat = conn.createStatement();
            rs = stat.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static boolean update(String sql) {
        Connection conn = null;
        try {
            conn = getConnection();
            Statement stat = conn.createStatement();
            stat.executeUpdate(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
