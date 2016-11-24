package Codo.Util;

/**
 * Created by terrychan on 23/11/2016.
 */
public interface CONSTANT {
    interface DATABASE {
        String HOST = "192.168.1.124";
        String PORT = "3306";
        String DATABASE_NAME = "Codo";
        String USERNAME = "root";
        String PASSWORD = "fantasy";
        String SALT = "https://itun.es/hk/zwLMW?i=815423920";
    }

    interface TABLE {
        String USER = "user";
        String TOKEN = "token";
        String CHANNEL = "channel";
    }

    interface CHANNEL {
        String UNSUBSCRIBE = "0";
        String CREATER = "1";
        String SUBSCRIBE = "2";
    }
}
