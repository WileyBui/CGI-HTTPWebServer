import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CreateSubAccount extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        String      usernameID = request.getParameter("userID");
        HttpSession session    = request.getSession();
        if (usernameID == null) {
            usernameID = (String)session.getAttribute("usernameID");
        } else {
            session.setAttribute("usernameID", usernameID);
        }

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html>");
        out.println("<html>");
        out.println("<head>");
        out.println("  <title>Opening an Account</title>");
        out.println("  <META HTTP-EQUIV='Cache-Control' CONTENT='no-cache'>");
		out.println("  <META HTTP-EQUIV='Pragma' CONTENT='no-cache'>");
		out.println("  <META HTTP-EQUIV='Expires' CONTENT='0'>");
        out.println("</head>");
        out.println("<body>");
        out.println("  <center>");
        out.println("    <h1><u style='color: #e62739'>Opening an Account</u></h1>");
        out.println("    <form method=POST action='CreateSubAccountProcessor'>");
        out.println("      <table>");
        out.println("        <tr>");
        out.println("          <td><strong>Account Type</strong></td>");
        out.println("          <td>");
        out.println("            <select name='new-subaccount-type' style='width: 100%'>");
        out.println("              <option value='checking' selected='selected'>Checking</option>");
        out.println("              <option value='saving'>Saving</option>");
        out.println("              <option value='brokerage'>Brokerage</option>");
        out.println("            </select>");
        out.println("          </td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td><strong>Initial Deposit ($)</strong></td>");
        out.println("          <td><input type='number' step='0.01' min='0' required name='new-subaccount-initial-deposit' placeholder='420.99' style='width:100%'></td>");
        out.println("        </tr>");
        out.println("      </table>");
        out.println("      <input type='submit' value='Submit'></td>");
        out.println("    </form>");
        out.println("  </center>");
        out.println("</body>");
        out.println("<style>");
        out.println("  body {");
        out.println("    background: linear-gradient(to right, #90f2f9, #66a6ff);");
        out.println("    margin-top: 25vh;");
        out.println("  }");
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
        out.println("  form {");
        out.println("    margin-top: 7vh;");
        out.println("  }");
        out.println("  table {");
        out.println("    padding-bottom: 3vh;");
        out.println("  }");
        out.println("</style>");
        out.println("</html>");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        doPost(request, response);
    }

}