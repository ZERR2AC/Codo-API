package pub.codo.Router.Channel;

import pub.codo.Controller.ChannelController.ChannelsController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by terrychan on 08/12/2016.
 */
public class Channel extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ChannelsController channelsController = new ChannelsController(req, resp);
        channelsController.getUserChannel();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ChannelsController channelsController = new ChannelsController(req, resp);
        channelsController.createChannel();
    }
}
