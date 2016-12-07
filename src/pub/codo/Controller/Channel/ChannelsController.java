package pub.codo.Controller.Channel;

import pub.codo.Controller.AuthController;
import pub.codo.Model.Channel;
import pub.codo.Util.CONSTANT.STATE;
import pub.codo.Util.CONSTANT.CHANNEL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Created by terrychan on 06/12/2016.
 */
public class ChannelsController extends AuthController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);

        int[] typeAcceptedValues = {CHANNEL.CREATER, CHANNEL.SUBSCRIBE, CHANNEL.UNSUBSCRIBE};
        if (!in("type", typeAcceptedValues)) setResponse(STATE.PARAMETER_ERROR, "type error.");
        else setResponseChannels(Channel.getChannels(user.getId(), req.getParameter("type")));
        makeResponse();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        String[] parameters = {"name"};

        if (require(parameters) && notEmpty(parameters)) {
            Channel channel = Channel.create(req.getParameter("name"), user.getId());
            if (channel == null) setResponse(STATE.NAME_DUPLICATED, "channel name has benn used.");
            else setResponseChannel(channel);
        } else {
            setResponse(STATE.PARAMETER_ERROR, "name could not be empty.");
        }
        makeResponse();
    }
}