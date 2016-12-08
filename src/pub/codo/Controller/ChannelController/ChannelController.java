package pub.codo.Controller.ChannelController;

import pub.codo.Controller.BaseController.RestURLIdController;
import pub.codo.Model.Channel;
import pub.codo.Util.CONSTANT;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by terrychan on 08/12/2016.
 */
public class ChannelController extends RestURLIdController {
    public ChannelController(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        super(httpServletRequest, httpServletResponse);
    }

    public void joinOrExitChannel() {
        String[] parameters = {"action"};
        int[] actionAcceptedValues = {CONSTANT.CHANNEL.ACTION_JOIN, CONSTANT.CHANNEL.ACTION_EXIT};

        if (require(parameters) && in("name", actionAcceptedValues)) {
            switch (getIntParameter("action")) {
                case CONSTANT.CHANNEL.ACTION_JOIN:
                    if (!Channel.joinChannel(user.getId(), resourceId))
                        setResponse(CONSTANT.STATE.ACTION_FAIL, "join channel fail.");
                    break;
                case CONSTANT.CHANNEL.ACTION_EXIT:
                    if (!Channel.exitChannel(user.getId(), resourceId))
                        setResponse(CONSTANT.STATE.ACTION_FAIL, "exit channel fail.");
                    break;
            }
        } else {
            setResponse(CONSTANT.STATE.PARAMETER_ERROR, "action code error.");
        }
        makeResponse();
    }
}
