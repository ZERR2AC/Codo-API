package pub.codo.Controller.Reminder;

import pub.codo.Controller.RestURLIdController;
import pub.codo.Model.Reminder;
import pub.codo.Util.CONSTANT.REMINDER;
import pub.codo.Util.CONSTANT.STATE;
import pub.codo.Util.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by terrychan on 07/12/2016.
 */
public class ReminderController extends RestURLIdController {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        boolean isCreator = Reminder.isCreator(resourceId, user.getId());
        boolean hasReminder = Reminder.hasReminder(resourceId, user.getId());
        String timestamp = Timestamp.getTime();

        if (isCreator) Reminder.notifyUpdate(resourceId, timestamp);
        else if (hasReminder) Reminder.notifyUpdate(resourceId, user.getId(), timestamp);

        if (isSet("title") && notEmpty("title") && isCreator) {
            String title = req.getParameter("title");
            Reminder.updateReminderTable(resourceId, "title", title);
        }

        if (isSet("content") && isCreator) {
            String content = req.getParameter("content");
            Reminder.updateReminderTable(resourceId, "content", content);
        }

        if (isSet("due") && notEmpty("due") && isCreator) {
            String due = req.getParameter("due");
            Reminder.updateReminderTable(resourceId, "due", due);
        }

        int[] priorityAcValues = {REMINDER.LOW, REMINDER.MED, REMINDER.HIGH};
        if (isSet("priority") && in("priority", priorityAcValues) && isCreator) {
            String priority = req.getParameter("priority");
            Reminder.updateReminderTable(resourceId, "priority", priority);
        }

        if (isSet("remark") && hasReminder) {
            String due = req.getParameter("remark");
            Reminder.updateUserReminderTable(resourceId, user.getId(), "remark", due);
        }

        int[] stateAcValues = {REMINDER.UNDO, REMINDER.COMPLETED};
        if (isSet("state") && in("state", stateAcValues) && hasReminder) {
            String state = req.getParameter("state");
            Reminder.updateUserReminderTable(resourceId, user.getId(), "state", state);
        }

        makeResponse();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
        if (Reminder.isCreator(resourceId, user.getId())) {
            if (!Reminder.createrDelete(resourceId, user.getId()))
                setResponse(STATE.ACTION_FAIL, "fail.");
        }
        else if (Reminder.hasReminder(resourceId, user.getId())) {
            if (!Reminder.subscribeDelete(resourceId, user.getId()))
                setResponse(STATE.ACTION_FAIL, "fail.");
        }
        else setResponse(STATE.PERMISSION_DENY, "permission deny.");
        makeResponse();
    }
}
