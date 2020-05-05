import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AccountBalances extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        String      username = request.getParameter("login-username");
        HttpSession session  = request.getSession();
        if (username == null) {
            username = (String)session.getAttribute("username");
        } else {
            session.setAttribute("username", username);
        }

        
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\" />");
        out.println("<META HTTP-EQUIV='Cache-Control' CONTENT='no-cache'>");
		out.println("<META HTTP-EQUIV='Pragma' CONTENT='no-cache'>");
		out.println("<META HTTP-EQUIV='Expires' CONTENT='0'>");
        
        ParentDatabase database      = new ParentDatabase();
        boolean        isParentExist = database.isParentExist(username);

        if (isParentExist) {
            out.println("<title>Account Summary</title>");
            session.setAttribute("usernameID", database.getParentID(username));
        } else {
            out.println("<title>Invalid Username</title>");
            out.println("<meta http-equiv = 'refresh' content = '5; url = index.htm' />");
        }
        
        out.println("</head>");

        out.println("<style>");
        out.println("body {");
        out.println("background: linear-gradient(to right, #66a6ff, #90f2f9);");
        out.println("}");
        out.println("</style>");
        out.println("<body>");

        if (!isParentExist) {
            out.println("<center><h2 style='color: red'>YOUR ACCOUNT CANNOT BE AUTHENTICATED.<br />YOU WILL BE REDIRECTED TO THE LOGIN SCREEN IN 5 SECONDS.</h2></center>");
        } else {
            out.println("<h3>Welcome to your account, " + username + ".</h3>");
            out.println("<br></br>");
            List<UserAccount> listOfSubAccounts = database.getParentObject(username).getSubAccounts();

            out.println("<button onclick=\"location.href = 'CreateSubAccount';\"'>Open a new account</a></button>");
            out.println("<button onclick=\"location.href = 'History';\"'>See " + username + "'s History</a></button>");
            out.println("<table>");
            out.println("<tr>");
            out.println("<th>Account Type</th>");
            out.println("<th>Account ID</th>");
            out.println("<th>Amount Left</th>");
            out.println("<th>Actions</th>");
            out.println("</tr>");
            for (UserAccount subAccount : listOfSubAccounts) {
                String accountID = subAccount.getAccountID();
                out.println("<tr>");
                out.println("<td>" + subAccount.getAccountType().toUpperCase() + "</td> ");
                out.println("<td>" + accountID + "</td>");
                out.println("<td>" + subAccount.getBalanceString() + "</td>");
                out.println("<td color='red'><ul>");
                out.println("<li><a href='Withdraw?accountID=" + accountID + "'>Withdraw</a></li>");
                out.println("<li><a href='CloseSubAccount?accountID=" + accountID + "'>Close Account</a></li>");
                
                if (listOfSubAccounts.size() > 1) {
                    out.println("<li>");
                    out.println("<form method=POST action='Transfer?transfer-from-ID=" + accountID + "'>");
                    out.println("<table>");
                    out.println("<tr>");
                    out.println("<td>");
                    out.println("Transfer to Account: <br />");
                    out.println("<select name='transfer-to-ID'>");
                    for (UserAccount sub : listOfSubAccounts) {
                        String ID = sub.getAccountID();
                        if (!accountID.equals(ID)) {
                            out.println("<option value='" + ID + "'>" + sub.getAccountType().toUpperCase() + "; ID: " + ID + "</option>");
                        }
                    }
                    out.println("</select>");
                    out.println("</td>");
                    out.println("<td>Amount: <br />");
                    out.println("<input type='number' step='0.01' min='0' required name='amount' placeholder='420.99'></td>");
                    out.println("<td><input type='submit' value='Submit'></td>");
                    out.println("</tr>");
                    out.println("</table>");
                    out.println("</form>");
                    out.println("</li>");
                } else {
                    out.println("<li>[Transfer disabled since you need 2+ accounts]</li>");
                }
                out.println("</ul>");
            }
            out.println("</body>");
            out.println("<style>table, th, td { border: 1px solid black; } button:hover {background-color: transparent;}");
            out.println("button { color: white; border: none; border-radius: 5px; padding: 10px; background-color: #004e8a; border: 0.5px solid white; }");
            out.println("button:hover{ cursor:pointer }");
            out.println("  input[type=text],");
            out.println("  input[type=number],");
            out.println("  select {");
            out.println("    border: none;");
            out.println("    border-radius: 5px;");
            out.println("    padding: 10px;");
            out.println("  }");
            out.println("  input[type=submit],");
            out.println("  input[type=button] {");
            out.println("    background-color: #87a7fd;");
            out.println("    color: white;");
            out.println("    padding: 9px 55px;");
            out.println("    border: none;");
            out.println("    border-radius: 4px;");
            out.println("    cursor: pointer;");
            out.println("    margin-left: 10px;");
            out.println("    margin-right: 10px;");
            out.println("    border: 0.5px solid white;");
            out.println("  }");
            out.println("  input[type=button] {");
            out.println("    background-color: #ff7381;");
            out.println("  }");
            out.println("  input[type=submit]:hover,");
            out.println("  input[type=button]:hover {");
            out.println("    background-color: transparent;");
            out.println("  }");
            out.println("  input:focus {");
            out.println("    outline: none;");
            out.println("  }");
            out.println("  a:hover {");
            out.println("    font-weight:bold;");
            out.println("  }");
            out.println("</style>");
            out.println("</html>");
        }
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        doPost(request, response);
    }

}