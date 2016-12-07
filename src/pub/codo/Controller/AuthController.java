package pub.codo.Controller;


import pub.codo.Util.CONSTANT.STATE;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by terrychan on 05/12/2016.
 */
public class AuthController extends Controller {
    // make all connection are valid
    private void checkToken() {
        if (user == null) {
            setResponse(STATE.TOKEN_INVALID, "token invalid.");
            makeResponse();
            // stop the function without throw error to response
            throw new ExceptionInInitializerError();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        checkToken();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        checkToken();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
        checkToken();
    }
}
