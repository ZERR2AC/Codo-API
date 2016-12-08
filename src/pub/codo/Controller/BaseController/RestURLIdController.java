package pub.codo.Controller.BaseController;


import pub.codo.Util.CONSTANT.STATE;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by terrychan on 07/12/2016.
 */
public class RestURLIdController extends AuthController {
    public RestURLIdController(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        super(httpServletRequest, httpServletResponse);

        String url = httpServletRequest.getRequestURI();
        String[] urls = url.split("/");
        try {
            resourceId = Integer.parseInt(urls[urls.length - 1]);
        } catch (Exception e) {
            setResponse(STATE.PARAMETER_ERROR, "url id invalid.");
            makeResponse();
            // stop the function without throw error to response
            throw new ExceptionInInitializerError();
        }
    }

    protected int resourceId;
}
