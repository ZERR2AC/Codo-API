package pub.codo.Controller.User;

import pub.codo.Controller.Controller;
import pub.codo.Model.User;
import pub.codo.Util.CONSTANT;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by terrychan on 05/12/2016.
 */
public class LoginController extends Controller {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        String[] parameters = {"username", "password"};

        // empty username or password
        if (require(parameters) && notEmpty(parameters)) {
            String username = req.getParameter("username").trim();
            String password = req.getParameter("password").trim();
            User user = User.login(username, password);
            // user not found
            if (user == null) setResponse(CONSTANT.STATE.PASSWORD_MISSMATCH, "username or password mismatch.");
            else setResponseUser(user);
        } else setResponse(CONSTANT.STATE.PARAMETER_ERROR, "parameter invalid.");
        makeResponse();
    }
}
