package Codo.Controller.User;

import Codo.Model.LoginSucceedResponse;
import Codo.Model.Response;
import Codo.Model.User;
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
public class Login extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username.isEmpty() || password.isEmpty()) {
            writer.write(Json.getGson().toJson(new Response(1, "username and password missmatch.")));
        } else {
            String token = User.getToken(username, password);
            if (token.isEmpty()) {
                writer.write(Json.getGson().toJson(new Response(1, "username and password missmatch.")));
            } else {
                String user_id = User.getUserId(username);
                writer.write(Json.getGson().toJson(new LoginSucceedResponse(0, "ok.", token, user_id)));
            }
        }
    }
}
