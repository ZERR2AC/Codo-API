package pub.codo.Controller.Channel;

import pub.codo.Controller.RestURLIdController;
import pub.codo.Model.Channel;
import pub.codo.Util.CONSTANT.STATE;
import pub.codo.Util.CONSTANT.CHANNEL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by terrychan on 07/12/2016.
 */
public class ChannelController extends RestURLIdController {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        String[] parameters = {"action"};
        int[] actionAcceptedValues = {CHANNEL.ACTION_JOIN, CHANNEL.ACTION_EXIT};

        if (require(parameters) && in("name", actionAcceptedValues)) {
            switch (Integer.parseInt(req.getParameter("action"))) {
                case CHANNEL.ACTION_JOIN:
                    if (!Channel.joinChannel(user.getId(), resourceId))
                        setResponse(STATE.ACTION_FAIL, "join channel fail.");
                    break;
                case CHANNEL.ACTION_EXIT:
                    if (!Channel.exitChannel(user.getId(), resourceId))
                        setResponse(STATE.ACTION_FAIL, "exit channel fail.");
                    break;
            }
        } else {
            setResponse(STATE.PARAMETER_ERROR, "action code error.");
        }
        makeResponse();
    }
}
