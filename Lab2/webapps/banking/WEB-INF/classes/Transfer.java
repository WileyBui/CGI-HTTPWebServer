import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Transfer extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        HttpSession    session        = request.getSession();
        PrintWriter    out            = response.getWriter();
        ParentDatabase database       = new ParentDatabase();
        String         transferFromID = request.getParameter("transfer-from-ID");
        String         transferToID   = request.getParameter("transfer-to-ID");
        Double         amount         = Double.parseDouble(request.getParameter("amount"));
        String         usernameID     = (String)session.getAttribute("usernameID");
        
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        out.println("<!DOCTYPE html><html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\" />");
        out.println("<META HTTP-EQUIV='Cache-Control' CONTENT='no-cache'>");
		out.println("<META HTTP-EQUIV='Pragma' CONTENT='no-cache'>");
		out.println("<META HTTP-EQUIV='Expires' CONTENT='0'>");
        
        ParentAccount listOfSubAccounts = database.getParentObjectByUsernameID(usernameID);
        if (listOfSubAccounts == null) {
            // "Two-Factor Authentication"
            out.println("<title>Transfer: Error!</title>");
            out.println("</head>");
            out.println("<body><center>");
            out.println("<h2 class='error'>Could not find the parent/main account (not transfer FROM or TO account).</h2>");
        } else {
            List<UserAccount> subAccounts = listOfSubAccounts.getSubAccounts();

            UserAccount subAccountFROM = null;
            UserAccount subAccountTO   = null;
            // Verifies if FROM and TO accounts are in the parent account
            for (UserAccount account : subAccounts) {
                if (account.getAccountID().equals(transferFromID)) {
                    subAccountFROM = account;
                    break;
                }
            }
            for (UserAccount account : subAccounts) {
                if (account.getAccountID().equals(transferToID)) {
                    subAccountTO = account;
                    break;
                }
            }

            if ((subAccountFROM != null) && (subAccountTO != null)) {
                // Verifies if an account has sufficient amount of money before transferring
                if (amount > subAccountFROM.getBalance()) {
                    out.println("<title>Transfer: Error!</title>");
                    out.println("</head>");
                    out.println("<body><center>");
                    out.println("<h2 class='error'>You must have sufficient funds from one account in order to transfer to another.</h2>");
                } else {
                    out.println("<title>Transfer: Success!</title>");
                    out.println("</head>");
                    out.println("<body><center>");
                    out.println("<h2 class='success'>Successfully transfered to " + subAccountTO.getAccountID() + "!</h2>");

                    // Transferring money between accounts
                    subAccountFROM.withdraw(amount);
                    subAccountTO.deposit(amount);

                    // Rewrites every object to ParentDatabase except the modified parent
                    try {
                        List<ParentAccount> allParentObjects = database.getAllParentObjects();
                        ObjectOutputStream  outputStream     = new ObjectOutputStream(new FileOutputStream("../webapps/banking/ParentDatabase.txt"));

                        // Clears the parent database
                        outputStream.reset();

                        for (ParentAccount parentObject : allParentObjects) {
                            if (parentObject.getUsernameID().equals(usernameID)) {
                                // Writes the modified parent
                                outputStream.writeObject(listOfSubAccounts);
                            } else {
                                // Rewrites existing parent(s)
                                outputStream.writeObject(parentObject);
                            }
                        }
                        outputStream.close();
                    } catch(Exception e) {}
                }
            } else {
                out.println("<title>Transfer: Error!</title>");
                out.println("</head>");
                out.println("<body><center>");
                out.println("<h2 class='error'>Cannot find either transfer FROM or TO account, or both.</h2>");
            }

            out.println("<p><a href='AccountBalances'>Return to Account Summary</a></p>");
            out.println("</center>");
        }

        out.println("</body>");
        out.println("<style>.error { color: red } .success { color: #6cc070; }</style>");
        out.println("</html>");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        doPost(request, response);
    }

}