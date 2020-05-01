import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CreateSubAccountProcessor extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter out     = response.getWriter();
        HttpSession session = request.getSession();


        String usernameID    = (String)session.getAttribute("usernameID");
        String accountType   = (String)request.getParameter("new-subaccount-type");
        String initialAmount = (String)request.getParameter("new-subaccount-initial-deposit");
        
        // Add a new subaccount to the parent account
        ParentAccount parentAccount = new ParentDatabase().getParentObjectByUsernameID(usernameID);
        UserAccount newChildAccount = new UserAccount(parentAccount.getUsernameID(), accountType, initialAmount);
        parentAccount.addSubAccount(newChildAccount);

        // File          databaseFile     = new File("../webapps/banking/ParentDatabase.txt");
        // ParentAccount newParentAccount = new ParentAccount(username);
        
        // // creating a child account & adding to parent account
        // newParentAccount.addSubAccount(newChildAccount);

        // if (databaseFile.length() == 0) {
        //     ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(databaseFile, true));
        //     outputStream.writeObject(newParentAccount);
        //     outputStream.close();
        // } else {
        //     AppendingObjectOutputStream outputStream = new AppendingObjectOutputStream(new FileOutputStream(databaseFile, true));
        //     outputStream.writeObject(newParentAccount);
        //     outputStream.close();
        // }

        // Logs log = new Logs();
        // log.appendToLog(username, "SUCCESS: ACCOUNT CREATED with " + newParentAccount.getBalanceString() + " to " + accountType);
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        out.println("<!DOCTYPE html><html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8' />");
        out.println("<title>Opening an Account: Success!</title>");
        // if (!isParentExist) {
            out.println("<meta http-equiv = 'refresh' content = '3; url = AccountBalances' />");
        // }
        out.println("</head>");
        out.println("<body bgcolor='#DCDCDC'>");
        out.println("<center>");
        out.println("<h2 class='success'>Successfully created a " + accountType + " account with $" + initialAmount + "!</h2>");
        out.println("<h4>Redirecting to Home page after 3 seconds...</h4>");


        out.println("<style>");
        out.println("body {");
        out.println("background: linear-gradient(to right, #66a6ff, #90f2f9);");
        out.println("}");
        out.println("</style>");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }
}