package pub.codo.Controller;

import pub.codo.Util.CONSTANT;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by terrychan on 06/12/2016.
 */
public class StatusController extends Controller {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        setResponse(CONSTANT.STATE.OK, "ok.");
        makeResponse();
    }
}
