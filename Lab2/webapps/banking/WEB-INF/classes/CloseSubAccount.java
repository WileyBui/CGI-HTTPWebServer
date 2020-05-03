import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CloseSubAccount extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter out         = response.getWriter();
        HttpSession session     = request.getSession(false);

        // we must verify BOTH usernameID AND accountID in order to withdraw money
        boolean accountFound = false;
        String  usernameID   = (String)session.getAttribute("usernameID");
        String  accountID    = request.getParameter("accountID");

        ParentAccount parentAccount   = new ParentDatabase().getParentObjectByUsernameID(usernameID);
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        out.println("<!DOCTYPE html><html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\" />");
        
        int isRemoved = parentAccount.removeSubAccountByAccountID(accountID);
            // if isRemoved is -2 => error  : needs to withdraw all money out first
            // if isRemoved is -1 => error  : could not find account
            // if isRemoved is  0 => success: removed sub account

        if (isRemoved < 0) {
            out.println("<title>Closing Account: Failed!</title>");
            out.println("</head>");
            out.println("<body><center>");
            
            if (isRemoved == -2) {
                out.println("<h2 class='error'>You must withdraw your balance before removing this account.</h2>");
            } else {
                out.println("<h2 class='error'>Account cannot be found</h2>");
            }
        } else {
            out.println("<title>Closing Account: Success!</title>");
            out.println("</head>");
            out.println("<body><center>");
            try {
                List<ParentAccount> allParentObjects = new ParentDatabase().getAllParentObjects();
                ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("../webapps/banking/ParentDatabase.txt"));
    
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

                out.println("<h2 class='success'>Successfully removed!</h2>");
            } catch(Exception e) {}
        }

        

        // Database    database          = new Database();
        // UserAccount currentUserObject = database.getUserObject(username);

        // Logs log = new Logs();
        // if (currentUserObject.getBalance() > 0) {
        //     out.println("<p class='error'>You must withdraw your money in order to close your account.</p>");
        //     out.println("<p><a href='Withdraw'>Withdraw Money</a></p>");
        //     log.appendToLog(username, "UNSUCCESS: ACCOUNT CLOSURE. Must withdraw " + currentUserObject.getBalanceString());
        // } else {
        //     out.println("<p>TODO: You have successfully closed your account.</p>");
        //     log.appendToLog(username, "SUCCESS: ACCOUNT CLOSURE");
        // }
        // out.println("</head>");
        // out.println("<body bgcolor=\"#DCDCDC\">");
        
        out.println("<p><a href='AccountBalances'>Return to Account Summary</a></p>");
        out.println("</center></body>");
        out.println("<style>.error { color: red } .success { color: #6cc070 }</style>");
        out.println("</head>");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }
}
