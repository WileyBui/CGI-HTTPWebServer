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

        // we must verify BOTH usernameID AND accountID in order to withdraw money
        boolean accountFound = false;
        String  usernameID   = (String)session.getAttribute("usernameID");
        String  accountID    = request.getParameter("accountID");


        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        out.println("<!DOCTYPE html><html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8' />");
        out.println("<META HTTP-EQUIV='Cache-Control' CONTENT='no-cache'>");
		out.println("<META HTTP-EQUIV='Pragma' CONTENT='no-cache'>");
		out.println("<META HTTP-EQUIV='Expires' CONTENT='0'>");
        out.println("<title>Withdraw Money</title>");
        out.println("</head>");
        out.println("<body>");
        
        ParentAccount parentAccount = new ParentDatabase().getParentObjectByUsernameID(usernameID);
        for (UserAccount account : parentAccount.getSubAccounts()) {
            if (account.getAccountID().equals(accountID)) {
                session.setAttribute("accountID", accountID);

                out.println("<form method=POST action='WithdrawProcessor'");
                out.println("<table>");
                out.println("<tr>");
                out.println("<td><strong>How much would you like to withdraw? Current Balance: " + account.getBalanceString() + "</strong></td>");
                out.println("<td><input type='number' step='0.01' min='0' required name='withdraw-amount' placeholder='420.69' style='width:100%'></td>");
                out.println("</tr>");
                out.println("</table>");
                out.println("<input type='submit' value='Submit'></td>");
                out.println("</form>");
                break;
            }
        }

        if (!accountFound) {
            out.println("Account cannot be found...");
        }
        out.println("</body>");
        out.println("<style>");
        out.println("body {");
        out.println("background: linear-gradient(to right, #66a6ff, #90f2f9);");
        out.println("}");
        out.println("</style>");
        out.println("</html>");

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        doPost(request, response);
    }

}