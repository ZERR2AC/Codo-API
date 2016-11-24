package Codo.Controller.User;

import Codo.Model.Response.LoginSucceedResponse;
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
public class LoginController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username.isEmpty() || password.isEmpty()) {
            writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.PASSWORD_MISSMATCH, "username and password missmatch.")));
        } else {
            String token = User.createToken(username, password);
            System.out.print(token);
            if (token.isEmpty()) {
                writer.write(Json.getGson().toJson(new Response(CONSTANT.STATE.PASSWORD_MISSMATCH, "username and password missmatch.")));
            } else {
                int user_id = User.getIdByName(username);
                writer.write(Json.getGson().toJson(new LoginSucceedResponse(token, user_id)));
            }
        }
    }
}
