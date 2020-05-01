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

        if(withdrawAmount > currentAmount) {
          out.println("<h1>Withdraw Failed. You tried to withdraw more than you have</h1>");
          out.println("<p><a href='AccountBalances'>Return to Account Summary</a></p>");
        } else {
          Double updatedAmount = currentAmount-withdrawAmount;
          currentUserObject.withdraw(withdrawAmount);
          out.println("<h1>Withdraw Successful. Your new balance is: " + currentUserObject.getBalanceString() + "</h1>");
          out.println("<p><a href='AccountBalances'>Return to Account Summary</a></p>");
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }
}