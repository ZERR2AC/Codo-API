package Codo.Controller.User;

import Codo.Model.Response.Response;
import Codo.Model.User;
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
public class RegisterController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        System.out.print(username + password);
        if (username.isEmpty() || password.isEmpty()) {
            writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.PARAMETER_EMPTY, "parameter is empty.")));
        } else {
            if (User.doRegister(username, password)) {
                writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.OK, "ok.")));
            } else {
                writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.NAME_DUPLICATED, "username has benn used.")));
            }
        }
    }
}
