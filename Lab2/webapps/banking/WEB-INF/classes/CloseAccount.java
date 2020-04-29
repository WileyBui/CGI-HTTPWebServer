import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CloseAccount extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter out     = response.getWriter();
        HttpSession session = request.getSession(false);

        String username = (String)session.getAttribute("username");

        Database    database          = new Database();
        UserAccount currentUserObject = database.getUserObject(username);

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        out.println("<!DOCTYPE html><html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\" />");
        out.println("<title>Closing Account: " + ((currentUserObject.getBalance() > 0)? "Error" : "Success") + "!</title>");

        Logs log = new Logs();
        if (currentUserObject.getBalance() > 0) {
            out.println("<p class='error'>You must withdraw your money in order to close your account.</p>");
            out.println("<p><a href='WithdrawMoney'>Withdraw Money</a></p>");
            log.appendToLog(username, "UNSUCCESS: ACCOUNT CLOSURE. Must withdraw " + currentUserObject.getBalanceString());
        } else {
            out.println("<p>TODO: You have successfully closed your account.</p>");
            log.appendToLog(username, "SUCCESS: ACCOUNT CLOSURE");
        }
        out.println("</head>");
        out.println("<body bgcolor=\"#DCDCDC\">");
        

        out.println("</body>");
        out.println("<style>.error { color: red }</style>");
        out.println("</head>");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }
}
