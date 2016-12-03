package Codo.Controller.Channel;

import Codo.Model.Channel;
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
 * Created by terrychan on 23/11/2016.
 */
public class ChannelController extends HttpServlet {
    // join or exit channel
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
            int channelId;
            int action;
            try {
                channelId = Integer.parseInt(urls[urls.length - 1]);
                action = Integer.parseInt(req.getParameter("action"));
            } catch (Exception e) {
                e.printStackTrace();
                writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.PARAMETER_EMPTY, "parameter invalid.")));
                return;
            }
            switch (action) {
                case CONSTANT.CHANNEL.ACTION_JOIN:
                    if (Channel.joinChannel(userId, channelId))
                        writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.OK, "ok")));
                    else
                        writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.ACTION_FAIL, "fail.")));
                    break;
                case CONSTANT.CHANNEL.ACTION_EXIT:
                    if (Channel.exitChannel(userId, channelId))
                        writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.OK, "ok")));
                    else
                        writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.ACTION_FAIL, "fail.")));
                    break;
                default:
                    writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.ACTION_FAIL, "fail.")));
                    break;
            }
        }
    }
}
