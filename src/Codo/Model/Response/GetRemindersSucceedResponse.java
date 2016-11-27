package Codo.Model.Response;

import Codo.Model.Reminder;
import Codo.Util.CONSTANT;

import java.util.List;

/**
 * Created by terrychan on 28/11/2016.
 */
public class GetRemindersSucceedResponse extends Response{
    List<Reminder> reminders;

    public GetRemindersSucceedResponse(List<Reminder> reminders) {
        super(CONSTANT.STATE.OK, "OK.");
        this.reminders = reminders;
    }
}
