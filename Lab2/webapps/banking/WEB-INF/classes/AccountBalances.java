import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AccountBalances extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        String username = request.getParameter("login-username");
        HttpSession session = request.getSession();
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

        Database database = new Database();
        
        if (database.isUserExist(username)) {
            out.println("<title>Account Summary</title>");
        } else {
            out.println("<title>Invalid Username</title>");
            out.println("<meta http-equiv = \"refresh\" content = \"2; url = index.htm\" />");
        }

        out.println("</head>");

        out.println("<style>");
        out.println("body {");
        out.println("background: linear-gradient(to right, #66a6ff, #90f2f9);");
        out.println("}");
        out.println("</style>");

        if (!database.isUserExist(username)) {
            out.println("<h3 style='color: black'>YOUR ACCOUNT CANNOT BE AUTHENTICATED. YOU WILL BE REDIRECTED TO THE LOGIN SCREEN IN 2 SECONDS</h>");
            return;
        }

    //    BALANCE:
        String balance = database.getUserObject(username).getBalanceString();

        out.println("<h3>Welcome to your account, " + username + "</h3>");
        out.println("<br></br>");
        //out.println("Current time : " + new Date(session.getLastAccessedTime()));
        out.println("<h3>Your current balance: " + balance + "</h3>");
        out.println("<h4 color='red'><a href='Withdraw'>Withdraw</a></h4>");


    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        doPost(request, response);
    }

}