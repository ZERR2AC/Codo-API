package pub.codo.Util;

/**
 * Created by terrychan on 23/11/2016.
 */
public interface CONSTANT {
    interface DATABASE {
        String HOST = "127.0.0.1";
        String PORT = "9696";
        String DATABASE_NAME = "Codo2";
        String USERNAME = "root";
        String PASSWORD = "QgQ6VBSF7i";
        String SALT = "https://itun.es/hk/zwLMW?i=815423920";
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
        int DATABASE_ERROR = -2;
        int NAME_DUPLICATED = 1;
        int PASSWORD_MISSMATCH = 2;
        int PARAMETER_ERROR = 3;
        int TOKEN_INVALID = 10;
        int PERMISSION_DENY = 11;
        int ACTION_FAIL = 20;
    }

    interface REMINDER {
        int PUBLIC = 0;
        int PRIVATE = 1;

        int UNDO = 0;
        int COMPLETED = 1;

        int LOW = 0;
        int MED = 1;
        int HIGH = 2;
    }
}
