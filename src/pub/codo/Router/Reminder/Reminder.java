package pub.codo.Router.Reminder;

import pub.codo.Controller.ReminderController.RemindersController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by terrychan on 08/12/2016.
 */
public class Reminder extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RemindersController remindersController = new RemindersController(req, resp);
        remindersController.getUserReminders();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RemindersController remindersController = new RemindersController(req, resp);
        remindersController.createReminder();
    }
}
