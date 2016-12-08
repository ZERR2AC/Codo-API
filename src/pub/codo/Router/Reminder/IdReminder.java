package pub.codo.Router.Reminder;

import pub.codo.Controller.ReminderController.ReminderController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by terrychan on 08/12/2016.
 */
public class IdReminder extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ReminderController reminderController = new ReminderController(req, resp);
        reminderController.updateReminder();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ReminderController reminderController = new ReminderController(req, resp);
        reminderController.deleteReminder();
    }
}
