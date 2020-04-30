import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ALLUSERS extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter  out     = response.getWriter();
        List<String> errors  = new ArrayList<>();
       
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        out.println("<!DOCTYPE html><html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\" />");
        out.println("<title>List of all users</title>");
        out.println("</head>");
        out.println("<body bgcolor=\"#DCDCDC\">");

        // Database database = new Database();
        // LIST OUT ALL USERS
        // List<UserAccount> accountList = database.getAllUserObjects();

        out.println("=========================================");
        out.println("<ul>All users:</ul>");
        // for (UserAccount account : accountList) {
        //     out.println("<li>" + account.getUsername() + "</li>");
        // }
        // out.println("</ul>");
        // END LISTING OUT ALL USERS

        out.println("</body>");
        out.println("<style>.error { color: red }</style>");
        out.println("</head>");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }
}
