package pub.codo.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by terrychan on 27/11/2016.
 */
public class Timestamp {
    static Date date;
    static SimpleDateFormat simpleDateFormat;

    public static String getTime() {
        if (date == null) date = new Date();
        if (simpleDateFormat == null) simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return simpleDateFormat.format(date);
    }
}
