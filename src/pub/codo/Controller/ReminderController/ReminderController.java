package pub.codo.Controller.ReminderController;

import pub.codo.Controller.BaseController.RestURLIdController;
import pub.codo.Model.Reminder;
import pub.codo.Util.CONSTANT;
import pub.codo.Util.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by terrychan on 08/12/2016.
 */
public class ReminderController extends RestURLIdController {
    public ReminderController(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        super(httpServletRequest, httpServletResponse);
    }

    public void updateReminder() {
        boolean isCreator = Reminder.isCreator(resourceId, user.getId());
        boolean hasReminder = Reminder.hasReminder(resourceId, user.getId());
        String timestamp = Timestamp.getTime();

        if (isCreator) Reminder.notifyUpdate(resourceId, timestamp);
        else if (hasReminder) Reminder.notifyUpdate(resourceId, user.getId(), timestamp);

        if (isSet("title") && notEmpty("title") && isCreator) {
            String title = getStringParameter("title");
            Reminder.updateReminderTable(resourceId, "title", title);
        }

        if (isSet("content") && isCreator) {
            String content = getStringParameter("content");
            Reminder.updateReminderTable(resourceId, "content", content);
        }

        if (isSet("due") && isCreator) {
            String due = getStringParameter("due");
            Reminder.updateReminderTable(resourceId, "due", due);
        }

        int[] priorityAcValues = {CONSTANT.REMINDER.LOW, CONSTANT.REMINDER.MED, CONSTANT.REMINDER.HIGH};
        if (isSet("priority") && in("priority", priorityAcValues) && isCreator) {
            String priority = getStringParameter("priority");
            Reminder.updateReminderTable(resourceId, "priority", priority);
        }

        if (isSet("remark") && hasReminder) {
            String due = getStringParameter("remark");
            Reminder.updateUserReminderTable(resourceId, user.getId(), "remark", due);
        }

        int[] stateAcValues = {CONSTANT.REMINDER.UNDO, CONSTANT.REMINDER.COMPLETED};
        if (isSet("state") && in("state", stateAcValues) && hasReminder) {
            String state = getStringParameter("state");
            Reminder.updateUserReminderTable(resourceId, user.getId(), "state", state);
        }

        makeResponse();
    }

    public void deleteReminder() {
        if (Reminder.isCreator(resourceId, user.getId())) {
            if (!Reminder.creatorDelete(resourceId, user.getId()))
                setResponse(CONSTANT.STATE.ACTION_FAIL, "fail.");
        } else if (Reminder.hasReminder(resourceId, user.getId())) {
            if (!Reminder.subscribeDelete(resourceId, user.getId()))
                setResponse(CONSTANT.STATE.ACTION_FAIL, "fail.");
        } else setResponse(CONSTANT.STATE.PERMISSION_DENY, "permission deny.");
        makeResponse();
    }
}
