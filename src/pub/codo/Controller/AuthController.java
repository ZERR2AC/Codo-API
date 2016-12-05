package pub.codo.Controller;

import pub.codo.Util.CONSTANT;

import javax.servlet.ServletException;

/**
 * Created by terrychan on 05/12/2016.
 */
public class AuthController extends Controller {
    @Override
    public void init() throws ServletException {
        super.init();
        if (user == null) {
            setResponse(CONSTANT.STATE.TOKEN_INVALID, "token invalid.");
            makeResponse();
        }
    }
}
