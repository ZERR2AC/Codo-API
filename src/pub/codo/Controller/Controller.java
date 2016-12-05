package pub.codo.Controller;

import pub.codo.Model.User;
import pub.codo.Util.Json;

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
    PrintWriter writer;
    User user;
    HttpServletRequest httpServletRequest;
    HttpServletResponse httpServletResponse;
    JsonResponse jsonResponse;

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

    private void init(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        httpServletRequest = req;
        httpServletResponse = resp;
        jsonResponse = new JsonResponse();
        setWriter(httpServletResponse);
        setUser(httpServletRequest);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        init(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        init(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        init(req, resp);
    }

    public void setResponse(int ret, String msg) {
        jsonResponse.ret = ret;
        jsonResponse.msg = msg;
    }

    public void setResponseUser(User user) {
        jsonResponse.user = user;
    }

    public void makeResponse() {
        writer.write(Json.getGson().toJson(jsonResponse));
        writer.close();
    }

    class JsonResponse {
        int ret;
        String msg;
        User user;
    }

    public boolean require(String[] parameters) {
        for (String parameter : parameters) {
            if (httpServletRequest.getParameter(parameter) == null) return false;
        }
        return true;
    }

    public boolean notEmpty(String[] parameters) {
        for (String parameter : parameters) {
            if (httpServletRequest.getParameter(parameter).trim().isEmpty()) return false;
        }
        return true;
    }
}



