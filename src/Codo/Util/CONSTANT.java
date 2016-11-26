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
        String USER_CHANNEL = "user_channel";
    }

    interface CHANNEL {
        int UNSUBSCRIBE = 0;
        int CREATER = 1;
        int SUBSCRIBE = 2;

        int ACTION_JOIN = 0;
        int ACTION_EXIT = 1;
    }

    interface STATE {
        int OK = 0;
        int ID_NOT_FOUND = -1;
        int NAME_DUPLICATED = 1;
        int PASSWORD_MISSMATCH = 2;
        int PARAMETER_EMPTY = 3;
        int TOKEN_INVALID = 10;
        int ACTION_FAIL = 20;
    }
}
