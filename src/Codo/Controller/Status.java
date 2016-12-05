package Codo.Controller;

import Codo.Model.Response.JsonResponse;
import Codo.Util.CONSTANT;
import Codo.Util.Json;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Created by terrychan on 23/11/2016.
 */

public class Status extends Controller {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        JsonResponse jsonResponse = new JsonResponse(CONSTANT.STATE.OK, "ok.", writer);
        jsonResponse.makeResponse();
    }
}
