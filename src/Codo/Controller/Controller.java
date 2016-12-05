package Codo.Controller;

import Codo.Model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by terrychan on 05/12/2016.
 */
public class Controller extends HttpServlet {
    protected PrintWriter writer;
    protected User user;

    private void setWriter(HttpServletResponse resp) throws ServletException, IOException {
        writer = resp.getWriter();
    }

    private void setUser(HttpServletRequest req) throws ServletException, IOException {
        String token = req.getParameter("token");
        if (token == null) user = null;
        else {
            user = User.getUserByToken(token);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setWriter(resp);
        setUser(req);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setWriter(resp);
        setUser(req);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setWriter(resp);
        setUser(req);
    }
}
