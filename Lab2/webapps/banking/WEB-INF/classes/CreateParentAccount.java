import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CreateParentAccount extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter out     = response.getWriter();
        HttpSession session = request.getSession();

        // INPUTS from client side
        String         initialAmount  = request.getParameter("initial-deposit");
        String         username       = request.getParameter("username");
        String         accountType    = request.getParameter("account-type");
        ParentDatabase parentDatabase = new ParentDatabase();

        // We now know that this user can be allocated!
        session.setAttribute("username", username);
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        out.println("<!DOCTYPE html><html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\" />");
        out.println("<title>Sign Up: " + (parentDatabase.isParentExist(username)? "Error" : "Success") + "!</title>");
        out.println("</head>");
        out.println("<body bgcolor=\"#DCDCDC\">");
        
        if (parentDatabase.isParentExist(username)) {
            out.println("<h3>Unable to create " + username + " account: Username already exists.</h3>");
        } else {
            // creating a parent account
            File          databaseFile     = new File("../webapps/banking/ParentDatabase.txt");
            ParentAccount newParentAccount = new ParentAccount(username);
            
            // creating a child account & adding to parent account
            UserAccount newChildAccount = new UserAccount(newParentAccount.getUsernameID(), accountType, initialAmount);
            newParentAccount.addSubAccount(newChildAccount);

            if (databaseFile.length() == 0) {
                ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(databaseFile, true));
                outputStream.writeObject(newParentAccount);
                outputStream.close();
            } else {
                AppendingObjectOutputStream outputStream = new AppendingObjectOutputStream(new FileOutputStream(databaseFile, true));
                outputStream.writeObject(newParentAccount);
                outputStream.close();
            }
    
            // Logs log = new Logs();
            // log.appendToLog(username, "SUCCESS: ACCOUNT CREATED with " + newParentAccount.getBalanceString() + " to " + accountType);
    
            out.println("<h3>Successfully created " + username + "!</h3>");
            // out.println("<h4>Your account type: " + accountType + "</h4>");
            // out.println("<h4>Your initial deposit: " + newParentAccount.getBalanceString() + "</h4>");
            out.println("<h4 color='red'><a href='CloseAccount'>CLOSE ACCOUNT</a></h4>");
        }

        out.println("</body>");
        out.println("<style>.error { color: red }</style>");
        out.println("</head>");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }
}
