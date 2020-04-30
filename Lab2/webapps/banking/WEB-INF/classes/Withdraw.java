import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Withdraw extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter out     = response.getWriter();
        HttpSession session = request.getSession(false);
        session.setAttribute("username", username);

        String username = (String)session.getAttribute("username");

        Database    database          = new Database();
        UserAccount currentUserObject = database.getUserObject(username);
        String      balance = currentUserObject.getBalanceString();

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        out.println("<style>");
        out.println("body {");
        out.println("background: linear-gradient(to right, #66a6ff, #90f2f9);");
        out.println("}");
        out.println("</style>");

        out.println("<form method=POST action=\"WithdrawProcessor\"");
        out.println("<table>");
        out.println("<tr>");
        out.println("<td><strong>How much would you like to withdraw? Current Balance : " + balance + "</strong></td>");
        out.println("<td><input type=\"number\" step=\"0.01\" min=\"0\" required name=\"withdraw-amount\" placeholder=\"420.69\" style=\"width:100%\"></td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("<input type=\"submit\" value=\"Submit\"></td>");
        out.println("</form>");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        doPost(request, response);
    }

}