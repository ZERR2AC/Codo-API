package Codo.Controller.Reminder;


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
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        int userId = User.getIdByToken(token);
        PrintWriter writer = resp.getWriter();
        if (userId == CONSTANT.STATE.ID_NOT_FOUND) {
            writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.TOKEN_INVALID, "token invalid.")));
        } else {
            String url = req.getRequestURI();
            String[] urls = url.split("/");
            int reminderId = Integer.parseInt(urls[urls.length - 1]);

        }
    }
}
