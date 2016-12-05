package Codo.Controller.User;

import Codo.Controller.Controller;
import Codo.Model.Response.LoginSucceedJsonResponse;
import Codo.Model.Response.JsonResponse;
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
public class LoginController extends HttpServlet {
    // user login
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        String username = req.getParameter("username").trim();
        String password = req.getParameter("password").trim();

        if (username.isEmpty() || password.isEmpty()) {
            writer.write(Json.getGson().toJson(new JsonResponse(CONSTANT.STATE.PASSWORD_MISSMATCH, "username and password missmatch.")));
        } else {
            String token = User.createToken(username, password);
            if (token.isEmpty()) {
                writer.write(Json.getGson().toJson(new JsonResponse(CONSTANT.STATE.PASSWORD_MISSMATCH, "username and password missmatch.")));
            } else {
                int user_id = User.getIdByName(username);
                writer.write(Json.getGson().toJson(new LoginSucceedJsonResponse(token, new User(user_id, username))));
            }
        }
    }
}
