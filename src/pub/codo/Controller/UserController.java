package pub.codo.Controller;

import pub.codo.Controller.BaseController.APIController;
import pub.codo.Model.User;
import pub.codo.Util.CONSTANT;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by terrychan on 08/12/2016.
 */
public class UserController extends APIController {
    public UserController(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        super(httpServletRequest, httpServletResponse);
    }

    public void login() {
        String[] parameters = {"username", "password"};

        // empty username or password
        if (require(parameters) && notEmpty(parameters) && isInt("expiry_time")) {
            String username = getStringParameter("username").trim();
            String password = getStringParameter("password").trim();
            int expiryTime;
            if (isSet("expiry_time")) expiryTime = getIntParameter("expiry_time");
            else expiryTime = CONSTANT.AUTH.DEFAULT_EXPIRYTIME;
            User user = User.login(username, password, expiryTime);
            // user not found
            if (user == null) setResponse(CONSTANT.STATE.PASSWORD_MISSMATCH, "username or password mismatch.");
            else setResponseUser(user);
        } else setResponse(CONSTANT.STATE.PARAMETER_ERROR, "parameter invalid.");
        makeResponse();
    }

    public void register() {
        String[] parameters = {"username", "password"};

        // empty username or password
        if (require(parameters) && notEmpty(parameters)) {
            String username = getStringParameter("username").trim();
            String password = getStringParameter("password").trim();
            User user = User.create(username, password);
            // object create fail, username must be unique
            if (user == null) setResponse(CONSTANT.STATE.ACTION_FAIL, "username has benn used.");
        } else setResponse(CONSTANT.STATE.PARAMETER_ERROR, "parameter invalid.");
        makeResponse();
    }
}
