package pub.codo.Controller.ReminderController;

import pub.codo.Controller.BaseController.AuthController;
import pub.codo.Model.Channel;
import pub.codo.Model.Reminder;
import pub.codo.Util.CONSTANT;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by terrychan on 08/12/2016.
 */
public class RemindersController extends AuthController {
    public RemindersController(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        super(httpServletRequest, httpServletResponse);
    }

    public void getUserReminders() {
        setResponseReminders(Reminder.getRemindersByUserId(user.getId()));
        makeResponse();
    }

    public void createrReminder() {
        String[] requiredParameters = {"title", "priority", "type"};
        int[] priorityAcceptedValues = {CONSTANT.REMINDER.LOW, CONSTANT.REMINDER.MED, CONSTANT.REMINDER.HIGH};
        int[] typeAcceptedValues = {CONSTANT.REMINDER.PUBLIC, CONSTANT.REMINDER.PRIVATE};

        if (require(requiredParameters) && notEmpty(requiredParameters) &&
                in("type", typeAcceptedValues) && in("priority", priorityAcceptedValues)) {
            // get parameters
            String title = getStringParameter("title");
            String content = getStringParameter("content");
            String due = getStringParameter("due");
            int priority = getIntParameter("priority");
            // end

            switch (getIntParameter("type")) {

                case CONSTANT.REMINDER.PUBLIC:
                    String[] requiredParameter = {"channel_id"};
                    if (!require(requiredParameter) || !notEmpty(requiredParameter) || !isInt(requiredParameter)) {
                        setResponse(CONSTANT.STATE.PARAMETER_ERROR, "parameters error.");
                        makeResponse();
                        return;
                    }
                    int channelId = getIntParameter("channel_id");
                    if (!Channel.isCreator(channelId, user.getId())) {
                        setResponse(CONSTANT.STATE.PERMISSION_DENY, "not your channel.");
                        makeResponse();
                        return;
                    }
                    Reminder publicReminder = Reminder.createPublicReminder(user.getId(), channelId, title, content, due, priority);
                    if (publicReminder == null) setResponse(CONSTANT.STATE.PARAMETER_ERROR, "parameters error.");
                    else setResponseReminder(publicReminder);
                    makeResponse();
                    return;

                case CONSTANT.REMINDER.PRIVATE:
                    Reminder privateReminder = Reminder.createPrivateReminder(user.getId(), title, content, due, priority);
                    if (privateReminder == null) setResponse(CONSTANT.STATE.PARAMETER_ERROR, "parameters error.");
                    else setResponseReminder(privateReminder);
                    makeResponse();
                    return;
            }
        } else {
            setResponse(CONSTANT.STATE.PARAMETER_ERROR, "parameters error.");
            makeResponse();
        }
    }
}
