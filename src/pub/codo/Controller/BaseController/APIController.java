package pub.codo.Controller.BaseController;

import pub.codo.Model.Channel;
import pub.codo.Model.Reminder;
import pub.codo.Model.User;
import pub.codo.Util.CONSTANT;
import pub.codo.Util.Json;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by terrychan on 05/12/2016.
 */
public class APIController {
    PrintWriter writer;
    HttpServletRequest httpServletRequest;
    HttpServletResponse httpServletResponse;
    JsonResponse jsonResponse;

    public APIController(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
        jsonResponse = new JsonResponse();
        writer = httpServletResponse.getWriter();
    }

    protected String getStringParameter(String s) {
        return httpServletRequest.getParameter(s);
    }

    protected int getIntParameter(String s) {
        return Integer.parseInt(getStringParameter(s));
    }

    protected void setResponse(int ret, String msg) {
        jsonResponse.ret = ret;
        jsonResponse.msg = msg;
    }

    protected void setResponseUser(User user) {
        jsonResponse.user = user;
    }

    protected void setResponseUsers(List<User> users) {
        jsonResponse.users = users;
    }

    protected void setResponseChannel(Channel channel) {
        jsonResponse.channel = channel;
    }

    protected void setResponseChannels(List<Channel> channels) {
        jsonResponse.channels = channels;
    }

    protected void setResponseReminders(List<Reminder> reminders) {
        jsonResponse.reminders = reminders;
    }

    protected void setResponseReminder(Reminder reminder) {
        jsonResponse.reminder = reminder;
    }

    public void makeResponse() {
        writer.write(Json.getGson().toJson(jsonResponse));
        writer.close();
    }

    class JsonResponse {
        int ret = CONSTANT.STATE.OK;
        String msg = "ok.";
        List<User> users;
        User user;
        List<Channel> channels;
        Channel channel;
        List<Reminder> reminders;
        Reminder reminder;
    }

    protected boolean require(String[] parameters) {
        for (String parameter : parameters) {
            if (getStringParameter(parameter) == null) return false;
        }
        return true;
    }

    protected boolean require(String parameter) {
        return getStringParameter(parameter) != null;
    }

    protected boolean notEmpty(String[] parameters) {
        for (String parameter : parameters) {
            String value = getStringParameter(parameter);
            if (value != null && value.trim().isEmpty()) return false;
        }
        return true;
    }

    protected boolean notEmpty(String parameter) {
        String value = getStringParameter(parameter);
        return !(value != null && value.trim().isEmpty());
    }

    protected boolean isInt(String[] parameters) {
        for (String parameter : parameters) {
            String value = getStringParameter(parameter);
            if (value != null)
                try {
                    Integer.parseInt(value);
                } catch (Exception e) {
                    return false;
                }
        }
        return true;
    }

    protected boolean isInt(String parameter) {
        String value = getStringParameter(parameter);
        if (value != null) {
            try {
                Integer.parseInt(value);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    protected boolean in(String parameter, int[] acceptedValues) {
        boolean isIn = false;
        try {
            String value = getStringParameter(parameter);
            for (int ac_value : acceptedValues) {
                if (value == null || Integer.parseInt(value) == ac_value) isIn = true;
            }
        } catch (Exception e) {
            return false;
        }
        return isIn;
    }

    protected boolean isSet(String parameter) {
        return getStringParameter(parameter) != null;
    }
}



