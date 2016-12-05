package pub.codo.Util;

/**
 * Created by terrychan on 23/11/2016.
 */
public interface CONSTANT {
    interface DATABASE {
    }

    interface TABLE {
        String USER = "user";
        String TOKEN = "token";
        String CHANNEL = "channel";
        String USER_CHANNEL = "user_channel";
        String REMINDER = "reminder";
        String USER_REMINDER = "user_reminder";
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
        int DATABASE_ERROR = -2;
        int NAME_DUPLICATED = 1;
        int PASSWORD_MISSMATCH = 2;
        int PARAMETER_EMPTY = 3;
        int TOKEN_INVALID = 10;
        int PERMISSION_DENY = 11;
        int ACTION_FAIL = 20;
    }

    interface REMINDER {
        int PUBLIC = 0;
        int PRIVATE = 1;

        int UNDO = 0;
        int COMPLETED = 1;
    }
}
