package pub.codo.Util;

import com.google.gson.Gson;

/**
 * Created by terrychan on 23/11/2016.
 */
public class Json {
    private static Gson gson;

    private Json() {
    }

    public static Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }
}
