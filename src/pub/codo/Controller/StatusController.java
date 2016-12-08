package pub.codo.Controller;

import pub.codo.Controller.BaseController.APIController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by terrychan on 06/12/2016.
 */
public class StatusController extends APIController {
    public StatusController(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        super(httpServletRequest, httpServletResponse);
    }

    public void responseStatus() {
        makeResponse();
    }
}
