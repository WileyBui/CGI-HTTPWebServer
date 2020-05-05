import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class WithdrawProcessor extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
      PrintWriter out     = response.getWriter();
      HttpSession session = request.getSession();
      
      Double withdrawAmount = Double.parseDouble(request.getParameter("withdraw-amount"));
      String usernameID     = (String)session.getAttribute("usernameID");
      String accountID      = (String)session.getAttribute("accountID");
        
      out.println("<!DOCTYPE html><html>");
      out.println("<head>");
      out.println("<meta charset='UTF-8' />");
      out.println("<META HTTP-EQUIV='Cache-Control' CONTENT='no-cache'>");
      out.println("<META HTTP-EQUIV='Pragma' CONTENT='no-cache'>");
      out.println("<META HTTP-EQUIV='Expires' CONTENT='0'>");
      out.println("<title>Withdraw Money</title>");
      out.println("</head>");
      out.println("<body>");

      
      ParentAccount parentAccount     = new ParentDatabase().getParentObjectByUsernameID(usernameID);
      boolean       isAccountModified = false;

      if (parentAccount == null) {
        out.println("<title>Withdraw: Error!</title>");
        out.println("</head>");
        out.println("<body><center>");
        // "Two-Factor Authentication"
        out.println("<h2 class='error'>Could not find the parent/main account.</h2>");
      } else {
        for (UserAccount account : parentAccount.getSubAccounts()) {
          if (account.getAccountID().equals(accountID)) {
            isAccountModified = true;
            if (withdrawAmount > account.getBalance()) {
              out.println("<h1>Withdraw Failed. You tried to withdraw more than you have</h1>");
              out.println("<p><a href='AccountBalances'>Return to Account Summary</a></p>");
            }
            else {
              account.withdraw(withdrawAmount);
              try {
                List<ParentAccount> allParentObjects = new ParentDatabase().getAllParentObjects();
                ObjectOutputStream  outputStream     = new ObjectOutputStream(new FileOutputStream("../webapps/banking/ParentDatabase.txt"));

                // Clears the parent database
                outputStream.reset();

                for (ParentAccount parentObject : allParentObjects) {
                    if (parentObject.getUsernameID().equals(usernameID)) {
                        // Writes the modified parent
                        outputStream.writeObject(parentAccount);
                    } else {
                        // Rewrites existing parent(s)
                        outputStream.writeObject(parentObject);
                    }
                }
                outputStream.close();
              } catch(Exception e) {}
              out.println("<h2>You successfully withdrew $" + String.format("%.2f", withdrawAmount) + ". Your new balance is: " + account.getBalanceString() + "</h2>");
              out.println("<p><a href='AccountBalances'>Return to Account Summary</a></p>");
            }
          }
        }

        if (!isAccountModified) {
          out.println("Account could not be found...");
          out.println("<p><a href='AccountBalances'>Return to Account Summary</a></p>");
        }

        out.println("</body>");
        out.println("<style>");
        out.println("body {");
        out.println("background: linear-gradient(to right, #66a6ff, #90f2f9);");
        out.println("}");
        out.println("</style>");
        out.println("</html>");
      }

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }
}