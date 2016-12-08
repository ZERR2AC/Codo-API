package pub.codo.Router.Channel;

import pub.codo.Controller.ChannelController.ChannelController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by terrychan on 08/12/2016.
 */
public class IdChannel extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ChannelController channelController = new ChannelController(req, resp);
        channelController.joinOrExitChannel();
    }
}
