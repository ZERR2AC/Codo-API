package pub.codo.Controller.Reminder;

import pub.codo.Controller.AuthController;
import pub.codo.Model.Channel;
import pub.codo.Model.Reminder;
import pub.codo.Util.CONSTANT.REMINDER;
import pub.codo.Util.CONSTANT.STATE;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by terrychan on 07/12/2016.
 */
public class RemindersController extends AuthController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        setResponseReminders(Reminder.getRemindersByUserId(user.getId()));
        makeResponse();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        String[] requiredParameters = {"title", "priority", "type"};
        int[] priorityAcceptedValues = {REMINDER.LOW, REMINDER.MED, REMINDER.HIGH};
        int[] typeAcceptedValues = {REMINDER.PUBLIC, REMINDER.PRIVATE};

        if (require(requiredParameters) && notEmpty(requiredParameters) &&
                in("type", typeAcceptedValues) && in("priority", priorityAcceptedValues)) {
            // get parameters
            String title = req.getParameter("title");
            String content = req.getParameter("content");
            String due = req.getParameter("due");
            int priority = Integer.parseInt(req.getParameter("priority"));
            // end

            switch (Integer.parseInt(req.getParameter("type"))) {

                case REMINDER.PUBLIC:
                    String[] requiredParameter = {"channel_id"};
                    if (!require(requiredParameter) || !notEmpty(requiredParameter) || !isInt(requiredParameter)) {
                        setResponse(STATE.PARAMETER_ERROR, "parameters error.");
                        makeResponse();
                        return;
                    }
                    int channelId = Integer.parseInt(req.getParameter("channel_id"));
                    if (!Channel.isCreator(channelId, user.getId())) {
                        setResponse(STATE.PERMISSION_DENY, "not your channel.");
                        makeResponse();
                        return;
                    }
                    Reminder publicReminder = Reminder.createPublicReminder(user.getId(), channelId, title, content, due, priority);
                    if (publicReminder == null) setResponse(STATE.PARAMETER_ERROR, "parameters error.");
                    else setResponseReminder(publicReminder);
                    makeResponse();
                    return;

                case REMINDER.PRIVATE:
                    Reminder privateReminder = Reminder.createPrivateReminder(user.getId(), title, content, due, priority);
                    if (privateReminder == null) setResponse(STATE.PARAMETER_ERROR, "parameters error.");
                    else setResponseReminder(privateReminder);
                    makeResponse();
                    return;
            }
        } else {
            setResponse(STATE.PARAMETER_ERROR, "parameters error.");
            makeResponse();
        }
    }
}
