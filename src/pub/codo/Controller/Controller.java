package pub.codo.Controller;

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
public class Controller extends HttpServlet {
    PrintWriter writer;
    protected User user;
    HttpServletRequest httpServletRequest;
    HttpServletResponse httpServletResponse;
    JsonResponse jsonResponse;

    private void setWriter(HttpServletResponse resp) throws ServletException, IOException {
        writer = resp.getWriter();
    }

    private void setUser(HttpServletRequest req) throws ServletException, IOException {
        String token = req.getParameter("token");
        if (token == null) user = null;
        else {
            user = User.getUserByToken(token);
        }
    }

    protected void init(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        httpServletRequest = req;
        httpServletResponse = resp;
        jsonResponse = new JsonResponse();
        setWriter(httpServletResponse);
        setUser(httpServletRequest);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        init(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        init(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        init(req, resp);
    }

    public void setResponse(int ret, String msg) {
        jsonResponse.ret = ret;
        jsonResponse.msg = msg;
    }

    public void setResponseUser(User user) {
        jsonResponse.user = user;
    }

    public void setResponseChannel(Channel channel) {
        jsonResponse.channel = channel;
    }

    public void setResponseChannels(List<Channel> channels) {
        jsonResponse.channels = channels;
    }

    public void setResponseReminders(List<Reminder> reminders) {
        jsonResponse.reminders = reminders;
    }

    public void setResponseReminder(Reminder reminder) {
        jsonResponse.reminder = reminder;
    }

    public void makeResponse() {
        writer.write(Json.getGson().toJson(jsonResponse));
        writer.close();
    }

    class JsonResponse {
        int ret = CONSTANT.STATE.OK;
        String msg = "ok.";
        User user;
        List<Channel> channels;
        Channel channel;
        List<Reminder> reminders;
        Reminder reminder;
    }

    public boolean require(String[] parameters) {
        for (String parameter : parameters) {
            if (httpServletRequest.getParameter(parameter) == null) return false;
        }
        return true;
    }

    public boolean notEmpty(String[] parameters) {
        for (String parameter : parameters) {
            String value = httpServletRequest.getParameter(parameter);
            if (value != null && value.trim().isEmpty()) return false;
        }
        return true;
    }

    public boolean notEmpty(String parameter) {
        String value = httpServletRequest.getParameter(parameter);
        if (value != null && value.trim().isEmpty()) return false;
        else return true;
    }

    public boolean isInt(String[] parameters) {
        for (String parameter : parameters) {
            String value = httpServletRequest.getParameter(parameter);
            if (value != null)
                try {
                    Integer.parseInt(value);
                } catch (Exception e) {
                    return false;
                }
        }
        return true;
    }

    public boolean in(String parameter, int[] acceptedValues) {
        boolean isIn = false;
        try {
            String value = httpServletRequest.getParameter(parameter);
            for (int ac_value : acceptedValues) {
                if (value == null || Integer.parseInt(value) == ac_value) isIn = true;
            }
        } catch (Exception e) {
            return false;
        }
        return isIn;
    }

    public boolean isSet(String parameter) {
        return httpServletRequest.getParameterValues(parameter) != null;
    }
}



