package pub.codo.Controller.ChannelController;

import pub.codo.Controller.BaseController.AuthController;
import pub.codo.Model.Channel;
import pub.codo.Util.CONSTANT;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by terrychan on 08/12/2016.
 */
public class ChannelsController extends AuthController {
    public ChannelsController(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        super(httpServletRequest, httpServletResponse);
    }

    public void getUserChannel() {
        int[] typeAcceptedValues = {CONSTANT.CHANNEL.CREATER, CONSTANT.CHANNEL.SUBSCRIBE, CONSTANT.CHANNEL.UNSUBSCRIBE};
        if (!in("type", typeAcceptedValues)) setResponse(CONSTANT.STATE.PARAMETER_ERROR, "type error.");
        else setResponseChannels(Channel.getChannels(user.getId(), getStringParameter("type")));
        makeResponse();
    }

    public void createChannel() {
        String[] parameters = {"name"};

        if (require(parameters) && notEmpty(parameters)) {
            Channel channel = Channel.create(getStringParameter("name"), user.getId());
            if (channel == null) setResponse(CONSTANT.STATE.NAME_DUPLICATED, "channel name has benn used.");
            else setResponseChannel(channel);
        } else {
            setResponse(CONSTANT.STATE.PARAMETER_ERROR, "name could not be empty.");
        }
        makeResponse();
    }
}
