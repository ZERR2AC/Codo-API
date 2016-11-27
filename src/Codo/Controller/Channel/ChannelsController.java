package Codo.Controller.Channel;

import Codo.Model.Channel;
import Codo.Model.Response.CreateChannelSucceedResponse;
import Codo.Model.Response.GetChannelsSucceedResponse;
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
 * Created by terrychan on 24/11/2016.
 */
public class ChannelsController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        int userId = User.getIdByToken(token);
        PrintWriter writer = resp.getWriter();
        if (userId == CONSTANT.STATE.ID_NOT_FOUND) {
            writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.TOKEN_INVALID, "token invalid.")));
        } else {
            String type = req.getParameter("type");
            if (type == null) type = "";
            writer.write(Json.getGson().toJson(new GetChannelsSucceedResponse(Channel.getChannels(userId, type))));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        int userId = User.getIdByToken(token);
        PrintWriter writer = resp.getWriter();
        if (userId == CONSTANT.STATE.ID_NOT_FOUND) {
            writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.TOKEN_INVALID, "token invalid.")));
        } else {
            String channelName = req.getParameter("name");
            Channel channel = Channel.newChannel(channelName, userId);
            if (channel == null) {
                writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.DATABASE_ERROR, "database error.")));
            } else {
                writer.write(Json.getGson().toJson(new CreateChannelSucceedResponse(channel)));
            }
        }
    }
}
