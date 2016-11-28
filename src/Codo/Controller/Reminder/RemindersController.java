package Codo.Controller.Reminder;

import Codo.Model.*;
import Codo.Model.Response.CreateReminderSucceedResponse;
import Codo.Model.Response.GetRemindersSucceedResponse;
import Codo.Model.Response.Response;
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
public class RemindersController extends HttpServlet {
    // get user's reminders
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        int userId = User.getIdByToken(token);
        PrintWriter writer = resp.getWriter();
        if (userId == CONSTANT.STATE.ID_NOT_FOUND) {
            writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.TOKEN_INVALID, "token invalid.")));
        } else {
            writer.write(Json.getGson().toJson(new GetRemindersSucceedResponse(Reminder.getRemindersByUserId(userId))));
        }
    }

    // create a new reminder
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        int userId = User.getIdByToken(token);
        PrintWriter writer = resp.getWriter();
        if (userId == CONSTANT.STATE.ID_NOT_FOUND) {
            writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.TOKEN_INVALID, "token invalid.")));
        } else {
            String title = req.getParameter("title");
            String content = req.getParameter("content");
            int type = Integer.parseInt(req.getParameter("type"));
            String due = req.getParameter("due");
            int priority = Integer.parseInt(req.getParameter("priority"));
            if (title.isEmpty() || content.isEmpty() || due.isEmpty()) {
                writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.PARAMETER_EMPTY, "parameter can not be empty.")));
            } else {
                switch (type) {
                    case CONSTANT.REMINDER.PUBLIC:
                        int channelId = Integer.parseInt(req.getParameter("channel_id"));
                        if (Channel.hasChannel(channelId) && Channel.isCreater(channelId, userId)) {
                            PublicReminder publicReminder = PublicReminder.newPublicReminder(userId, title, content, due, priority, channelId);
                            if (publicReminder != null)
                                writer.write(Json.getGson().toJson(new CreateReminderSucceedResponse(publicReminder)));
                            else
                                writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.ACTION_FAIL, "unknow error.")));
                        } else {
                            writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.ID_NOT_FOUND, "channel id error.")));
                        }
                        break;
                    case CONSTANT.REMINDER.PRIVATE:
                        // TODO: 29/11/2016 可以添加 remark
                        PrivateReminder privateReminder = PrivateReminder.newPrivateReminder(title, content, due, priority, userId);
                        writer.write(Json.getGson().toJson(new CreateReminderSucceedResponse(privateReminder)));
                        break;
                }
            }
        }
    }
}