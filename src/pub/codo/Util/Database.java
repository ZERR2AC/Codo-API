package pub.codo.Util;


import java.sql.*;

/**
 * Created by terrychan on 23/11/2016.
 */
public class Database {
    private static Connection connection;

    private Database() {
    }

    private static synchronized Connection getConnection() {
        String connectString = "jdbc:mysql://" + CONSTANT.DATABASE.HOST
                + ":" + CONSTANT.DATABASE.PORT
                + "/" + CONSTANT.DATABASE.DATABASE_NAME
                + "?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            if (connection == null)
                connection = DriverManager.getConnection(connectString, CONSTANT.DATABASE.USERNAME, CONSTANT.DATABASE.PASSWORD);
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResultSet query(String sql) {
        ResultSet resultSet = null;
        try {
            Connection conn = getConnection();
            Statement stat = conn.createStatement();
            resultSet = stat.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static boolean update(String sql) {
        try {
            Connection conn = getConnection();
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean delete(String sql) {
        try {
            Connection conn = getConnection();
            Statement statement = conn.createStatement();
            return statement.executeUpdate(sql) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int insert(String sql) {
        try {
            Connection conn = getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CONSTANT.STATE.DATABASE_ERROR;
    }
}
