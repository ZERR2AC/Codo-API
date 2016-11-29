package Codo.Model.Response;

import Codo.Model.Reminder;
import Codo.Util.CONSTANT;

/**
 * Created by terrychan on 27/11/2016.
 */
public class CreateReminderSucceedResponse extends Response {
    Reminder reminder;

    public CreateReminderSucceedResponse(Reminder reminder) {
        super(CONSTANT.STATE.OK, "ok.");
        this.reminder = reminder;
    }
}
