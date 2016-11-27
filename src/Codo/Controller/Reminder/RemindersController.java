package Codo.Controller.Reminder;

import Codo.Model.Channel;
import Codo.Model.PrivateReminder;
import Codo.Model.PublicReminder;
import Codo.Model.Response.CreateReminderSucceedResponse;
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
public class RemindersController extends HttpServlet {
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
                        PrivateReminder privateReminder = PrivateReminder.newPrivateReminder(title, content, due, priority, userId);
                        writer.write(Json.getGson().toJson(new CreateReminderSucceedResponse(privateReminder)));
                        break;
                }
            }
        }
    }
}
