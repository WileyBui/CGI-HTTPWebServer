import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class WithdrawProcessor extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter  out     = response.getWriter();
        HttpSession  session = request.getSession();
        List<String> errors  = new ArrayList<>();

        String username = (String)session.getAttribute("username");
        session.setAttribute("username", username);
        
        Database    database          = new Database();
        UserAccount currentUserObject = database.getUserObject(username);

        out.println("<style>");
        out.println("body {");
        out.println("background: linear-gradient(to right, #66a6ff, #90f2f9);");
        out.println("}");
        out.println("</style>");

        Double withdrawAmount = Double.parseDouble(request.getParameter("withdraw-amount"));
        Double currentAmount = currentUserObject.getBalance();
        Double newAmount = currentAmount-withdrawAmount;

        if(withdrawAmount > currentAmount) {
          out.println("<h1>Withdraw Failed. You tried to withdraw more than you have</h1>");
          out.println("<p><a href='AccountBalances'>Return to Account Summary</a></p>");
        } else {
          String oldFileName = "../webapps/banking/database.txt";
          String tmpFileName = "../webapps/banking/tmp_database.txt";
          
          //File              databaseFile = new File("../webapps/banking/database.txt");
          //ObjectInputStream objInput     = new ObjectInputStream(new FileInputStream(databaseFile));

          ObjectInputStream objInput = new ObjectInputStream(new FileInputStream(new File(oldFileName)));
          ObjectOutputStream objOutput = new ObjectOutputStream(new FileOutputStream(new File(tmpFileName)));


          try {
            Object obj = objInput.readObject();

            while (obj != null) {
              if(obj instanceof UserAccount) {
                UserAccount retrievedAccount = (UserAccount) obj;
                //out.println(retrievedAccount.getUsername());
                if(username.equals(retrievedAccount.getUsername())) {
                  retrievedAccount.withdraw(withdrawAmount);
                }
                objOutput.writeObject(retrievedAccount);
                objOutput.flush();
              }
              obj = objInput.readObject();
            }

          } catch (Exception e) {    
          }

          objInput.close();
          objOutput.close();

          //once everything is complete, delete old file
          File oldFile = new File(oldFileName);
          oldFile.delete();

          //and rename tmp file's name to old file name
          File newFile = new File(tmpFileName);
          newFile.renameTo(oldFile);

          out.println("<h2>You successfully withdrew " + Double.toString(withdrawAmount) + ". Your new balance is: " + Double.toString(newAmount) + "</h2>");
          out.println("<p><a href='AccountBalances'>Return to Account Summary</a></p>");
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }
}