package Codo.Controller.Reminder;


import Codo.Model.PrivateReminder;
import Codo.Model.Reminder;
import Codo.Model.Response.Response;
import Codo.Model.User;
import Codo.Util.CONSTANT;
import Codo.Util.Json;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by terrychan on 27/11/2016.
 */
public class ReminderController extends HttpServlet {
    // update reminder information
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        int userId = User.getIdByToken(token);
        PrintWriter writer = resp.getWriter();
        if (userId == CONSTANT.STATE.ID_NOT_FOUND) {
            writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.TOKEN_INVALID, "token invalid.")));
        } else {
            String url = req.getRequestURI();
            String[] urls = url.split("/");
            int reminderId = Integer.parseInt(urls[urls.length - 1]);
            int type = Reminder.getReminderTypeById(reminderId);
            if (type == CONSTANT.STATE.ID_NOT_FOUND) {
                writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.ID_NOT_FOUND, "reminder not found.")));
            } else {
                switch (type) {
                    case CONSTANT.REMINDER.PRIVATE:
                        PrivateReminder privateReminder = PrivateReminder.getReminderById(reminderId);
                        if (privateReminder != null && privateReminder.ownReminder(userId)) {
                            String title = req.getParameter("title");
                            if (title != null) privateReminder.title = title;
                            String content = req.getParameter("content");
                            if (content != null) privateReminder.content = content;
                            String due = req.getParameter("due");
                            if (due != null) privateReminder.due = due;
                            String remark = req.getParameter("remark");
                            if (remark != null) privateReminder.remark = remark;
                            String priority = req.getParameter("priority");
                            if (priority != null) privateReminder.priority = Integer.parseInt(priority);
                            String state = req.getParameter("state");
                            if (state != null) privateReminder.state = Integer.parseInt(state);
                            // TODO: 29/11/2016 Check parameter
                            if (privateReminder.save()) {
                                writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.OK, "OK.")));
                            } else {
                                writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.ACTION_FAIL, "action fail.")));
                            }
                        } else {
                            writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.PERMISSION_DENY, "not your reminder.")));
                        }
                        break;
                    case CONSTANT.REMINDER.PUBLIC:
                        break;
                }
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
