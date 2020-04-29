import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CreateAccount extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter  out     = response.getWriter();
        HttpSession  session = request.getSession();
        List<String> errors  = new ArrayList<>();

        // INPUTS from client side
        double initialAmount;
        String username    = request.getParameter("username");
        String accountType = request.getParameter("account-type");
        
        // CONVERT $ from string to double.
        try { 
            initialAmount = Double.parseDouble(request.getParameter("initial-deposit"));
        } catch (NumberFormatException e) {
            initialAmount = -1;
        }
        
        // ERROR CHECK: prevent user from doing Inspect Element from client side
        if (initialAmount < 0) {
            errors.add("The initial deposit amount you entered is incorrect.");
        }
        if (!(accountType.equals("checking-account") || accountType.equals("saving-account") || accountType.equals("brokerage-account"))) {
            errors.add("Account type must be checking, saving, or brokerage.");
        }
        if (username.length() < 1) {
            errors.add("Username must have at least 1 character.");
        }

        Database database = new Database();
        if (database.isUserExist(username)) {
            errors.add("Username already exists.");
        }

        // We now know that this user can be allocated!
        session.setAttribute("username", username);
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        out.println("<!DOCTYPE html><html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\" />");
        out.println("<title>Sign Up: " + ((errors.size() > 0)? "Error" : "Success") + "!</title>");
        out.println("</head>");
        out.println("<body bgcolor=\"#DCDCDC\">");
        

        if (errors.size() > 0) {
            out.println("<h3>Unable to create " + username+ " account!</h3>");
            out.println("<ul>");
            for (String error: errors) {
                out.println("<li class='error'>Error: " + error + "</li>");
            }
            out.println("</ul>");
        } else {
            File        databaseFile = new File("database.txt");
            UserAccount newAccount   = new UserAccount(username, accountType, initialAmount);
            if (databaseFile.length() == 0) {
                ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(databaseFile, true));
                outputStream.writeObject(newAccount);
                outputStream.close();
            } else {
                AppendingObjectOutputStream outputStream = new AppendingObjectOutputStream(new FileOutputStream(databaseFile, true));
                outputStream.writeObject(newAccount);
                outputStream.close();
            }

            out.println("<h3>Successfully created " + username + "!</h3>");
            out.println("<h4>Your account type: " + accountType + "</h4>");
            out.println("<h4>Your initial deposit: $" + String.format("%.2f", initialAmount) + "</h4>");
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

    public class AppendingObjectOutputStream extends ObjectOutputStream {
        public AppendingObjectOutputStream(OutputStream out) throws IOException {
          super(out);
        }
      
        @Override
        protected void writeStreamHeader() throws IOException {
          reset();
        }
    }
}
