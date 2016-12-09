package pub.codo.Controller.BaseController;


import pub.codo.Model.User;
import pub.codo.Util.CONSTANT.STATE;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by terrychan on 05/12/2016.
 */
public class AuthController extends APIController {
    public AuthController(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        super(httpServletRequest, httpServletResponse);

        String token = getStringParameter("token");
        if (token == null) user = null;
        else {
            user = User.getUserByToken(token);
            setResponseUser(user);
        }
        if (user == null) {
            setResponse(STATE.TOKEN_INVALID, "token invalid.");
            makeResponse();
            // stop the function without throw error to response
            throw new ExceptionInInitializerError();
        }
    }
}
