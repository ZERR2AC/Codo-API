package Codo.Controller.Channel;

import Codo.Model.Channel;
import Codo.Model.Response.CreateChannelSucceedResponse;
import Codo.Model.Response.Response;
import Codo.Model.User;
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
public class ChannelController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        String userId = User.getIdByToken(token);
        PrintWriter writer = resp.getWriter();
        if (userId.isEmpty()) {
            writer.write(Json.getGson().toJson(new Response(10, "token invalid.")));
        } else {
            String channelName = req.getParameter("name");
            Channel channel = Channel.newChannel(channelName, userId);
            if (channel == null) {
                writer.write(Json.getGson().toJson(new Response(1, "channel name has benn used.")));
            } else {
                writer.write(Json.getGson().toJson(new CreateChannelSucceedResponse(channel)));
            }
        }
    }
}
