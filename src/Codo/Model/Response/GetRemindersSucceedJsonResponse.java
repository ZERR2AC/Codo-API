package Codo.Model.Response;

import Codo.Model.Reminder;
import Codo.Util.CONSTANT;

import java.util.List;

/**
 * Created by terrychan on 28/11/2016.
 */
public class GetRemindersSucceedJsonResponse extends JsonResponse {
    List<Reminder> reminders;

    public GetRemindersSucceedJsonResponse(List<Reminder> reminders) {
        super(CONSTANT.STATE.OK, "OK.");
        this.reminders = reminders;
    }
}
