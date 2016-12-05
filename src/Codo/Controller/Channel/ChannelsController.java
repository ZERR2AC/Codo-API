package Codo.Controller.Channel;

import Codo.Model.Channel;
import Codo.Model.Response.CreateChannelSucceedJsonResponse;
import Codo.Model.Response.GetChannelsSucceedJsonResponse;
import Codo.Model.Response.JsonResponse;
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
    // get all channels in server
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        int userId = User.getIdByToken(token);
        PrintWriter writer = resp.getWriter();
        if (userId == CONSTANT.STATE.ID_NOT_FOUND) {
            writer.write(Json.getGson().toJson(new JsonResponse(CONSTANT.STATE.TOKEN_INVALID, "token invalid.")));
        } else {
            String type = req.getParameter("type");
            if (type == null) type = "";
            writer.write(Json.getGson().toJson(new GetChannelsSucceedJsonResponse(Channel.getChannels(userId, type))));
        }
    }

    // create a new channel
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        int userId = User.getIdByToken(token);
        PrintWriter writer = resp.getWriter();
        if (userId == CONSTANT.STATE.ID_NOT_FOUND) {
            writer.write(Json.getGson().toJson(new JsonResponse(CONSTANT.STATE.TOKEN_INVALID, "token invalid.")));
        } else {
            String channelName = req.getParameter("name");
            Channel channel = Channel.newChannel(channelName, userId);
            if (channel == null) {
                writer.write(Json.getGson().toJson(new JsonResponse(CONSTANT.STATE.NAME_DUPLICATED, "channel name duplicated error.")));
            } else {
                Channel.joinChannel(userId, channel.id);
                writer.write(Json.getGson().toJson(new CreateChannelSucceedJsonResponse(channel)));
            }
        }
    }
}
