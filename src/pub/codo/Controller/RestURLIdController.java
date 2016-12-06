package pub.codo.Controller;


import pub.codo.Util.CONSTANT;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by terrychan on 07/12/2016.
 */
public class RestURLIdController extends AuthController {
    protected int resourceId;

    protected void getRestURLId() {
        String url = httpServletRequest.getRequestURI();
        String[] urls = url.split("/");
        try {
            resourceId = Integer.parseInt(urls[urls.length - 1]);
        } catch (Exception e) {
            setResponse(CONSTANT.STATE.PARAMETER_ERROR, "url id invalid.");
            makeResponse();
            // stop the function without throw error to response
            throw new ExceptionInInitializerError();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        getRestURLId();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        getRestURLId();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
        getRestURLId();
    }
}
