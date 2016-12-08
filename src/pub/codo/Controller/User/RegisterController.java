package pub.codo.Controller.User;

import pub.codo.Controller.Controller;
import pub.codo.Model.User;
import pub.codo.Util.CONSTANT.STATE;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by terrychan on 05/12/2016.
 */
public class RegisterController extends Controller {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        String[] parameters = {"username", "password"};

        // empty username or password
        if (require(parameters) && notEmpty(parameters)) {
            String username = req.getParameter("username").trim();
            String password = req.getParameter("password").trim();
            User user = User.create(username, password);
            // object create fail, username must be unique
            if (user == null) setResponse(STATE.ACTION_FAIL, "username has benn used.");
        } else setResponse(STATE.PARAMETER_ERROR, "parameter invalid.");
        makeResponse();
    }
}