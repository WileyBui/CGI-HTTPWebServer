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

        // LIST OUT ALL USERS
        ParentDatabase database = new ParentDatabase();
        List<ParentAccount> accountList = database.getAllParentObjects();

        out.println("<h3>All users:</h3>");
        out.println("=========================================");
        out.println("<table>");
        out.println("<tr><th>Username</th>");
        out.println("<th>UsernameID</th>");
        out.println("<th>Account(s)</th></tr>");

        for (ParentAccount account : accountList) {
            List<UserAccount> subAccountsFromParent = account.getSubAccounts();
            out.println("<tr>");
            out.println("<td>" + account.getUsername() + "</td>");
            out.println("<td>" + account.getUsernameID() + "</td>");
            out.println("<td><ul>");
            for (UserAccount subAccount : subAccountsFromParent) {
                out.println("<li><strong>" + subAccount.getAccountType() + "</strong> ");
                out.println(subAccount.getBalanceString() + "</li>");
            }
            out.println("</tr>");
        }
        out.println("</ul>");
        // END LISTING OUT ALL USERS

        out.println("</body>");
        out.println("<style>.error { color: red }</style>");
        out.println("</html>");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }
}
