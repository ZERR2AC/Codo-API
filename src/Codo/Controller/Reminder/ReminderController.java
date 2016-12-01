package Codo.Controller.Reminder;


import Codo.Model.*;
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
public class ReminderController extends HttpServlet {
    // update reminder information
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // check token
        String token = req.getParameter("token");
        int userId = User.getIdByToken(token);
        PrintWriter writer = resp.getWriter();
        if (userId == CONSTANT.STATE.ID_NOT_FOUND) {
            writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.TOKEN_INVALID, "token invalid.")));
        } else {
            // get reminder id
            String url = req.getRequestURI();
            String[] urls = url.split("/");
            int reminderId = Integer.parseInt(urls[urls.length - 1]);
            // get reminder type
            int type = Reminder.getReminderTypeById(reminderId);
            if (type == CONSTANT.STATE.ID_NOT_FOUND) {
                writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.ID_NOT_FOUND, "reminder not found.")));
            } else {
                switch (type) {
                    case CONSTANT.REMINDER.PRIVATE:
                        PrivateReminder privateReminder = PrivateReminder.getReminderById(reminderId);
                        if (privateReminder != null && privateReminder.ownReminder(userId)) {
                            // get parameters
                            String title = req.getParameter("title");
                            if (title != null) privateReminder.title = title;

                            String content = req.getParameter("content");
                            if (content != null) privateReminder.content = content;
                            else privateReminder.content = "";

                            String due = req.getParameter("due");
                            if (due != null) privateReminder.due = due;
                            else privateReminder.due = "";

                            String remark = req.getParameter("remark");
                            if (remark != null) privateReminder.remark = remark;

                            String priority = req.getParameter("priority");
                            if (priority != null) privateReminder.priority = Integer.parseInt(priority);

                            String state = req.getParameter("state");
                            if (state != null) privateReminder.state = Integer.parseInt(state);
                            // TODO: 29/11/2016 Check parameter

                            if (!privateReminder.save()) {
                                writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.ACTION_FAIL, "action fail, mostly parameter error.")));
                                return;
                            }
                        } else {
                            writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.PERMISSION_DENY, "permission deny.")));
                            return;
                        }
                        break;
                    case CONSTANT.REMINDER.PUBLIC:
                        if (PublicReminder.isCreater(reminderId, userId)) {
                            // edit title content due priority
                            // update all user_reminder last_update
                            // update channel last_update
                            String title = req.getParameter("title");
                            if (title == null) title = "";

                            String content = req.getParameter("content");
                            if (content == null) content = "";

                            String due = req.getParameter("due");
                            if (due == null) due = "";

                            String priority = req.getParameter("priority");
                            if (priority == null) priority = "";

                            String remark = req.getParameter("remark");
                            if (remark != null) {
                                if (!PublicReminder.updateRemark(reminderId, userId, remark)) {
                                    writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.ACTION_FAIL, "action fail.")));
                                    return;
                                }
                            }

                            String state = req.getParameter("state");
                            if (state != null) {
                                if (!PublicReminder.updateState(reminderId, userId, Integer.parseInt(state))) {
                                    writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.ACTION_FAIL, "action fail.")));
                                    return;
                                }
                            }

                            if (!PublicReminder.update(reminderId, title, content, due, priority)) {
                                writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.ACTION_FAIL, "action fail.")));
                                return;
                            }
                        } else {
                            // edit remark and state
                            // update user_reminder last_update
                            String remark = req.getParameter("remark");
                            String state = req.getParameter("state");
                            if (remark != null) {
                                if (!PublicReminder.updateRemark(reminderId, userId, remark)) {
                                    writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.ACTION_FAIL, "action fail.")));
                                    return;
                                }
                            }
                            if (state != null) {
                                if (!PublicReminder.updateState(reminderId, userId, Integer.parseInt(state))) {
                                    writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.ACTION_FAIL, "action fail.")));
                                    return;
                                }
                            }
                        }
                        break;
                }
                writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.OK, "ok.")));
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // check token
        String token = req.getParameter("token");
        int userId = User.getIdByToken(token);
        PrintWriter writer = resp.getWriter();
        if (userId == CONSTANT.STATE.ID_NOT_FOUND) {
            writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.TOKEN_INVALID, "token invalid.")));
        } else {
            // get reminder id
            String url = req.getRequestURI();
            String[] urls = url.split("/");
            int reminderId = Integer.parseInt(urls[urls.length - 1]);
            if (Reminder.isCreater(reminderId, userId)) {
                if (Reminder.createrDelete(reminderId)) {
                    writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.OK, "ok.")));
                } else {
                    writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.ACTION_FAIL, "action fail.")));
                }
            } else {
                if (Reminder.subscribeDelete(reminderId, userId)) {
                    writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.OK, "ok.")));
                } else {
                    writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.PERMISSION_DENY, "permission deny.")));
                }
            }
        }
    }
}
