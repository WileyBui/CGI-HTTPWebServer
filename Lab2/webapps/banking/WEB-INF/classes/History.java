import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class History extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter out     = response.getWriter();
        HttpSession session = request.getSession();

        String username = (String)session.getAttribute("username");

        out.println("<!DOCTYPE html><html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\" />");
        out.println("<META HTTP-EQUIV='Cache-Control' CONTENT='no-cache'>");
		out.println("<META HTTP-EQUIV='Pragma' CONTENT='no-cache'>");
		out.println("<META HTTP-EQUIV='Expires' CONTENT='0'>");
        out.println("<title>" + username + "'s History!</title>");
        out.println("</head>");
        out.println("<body bgcolor=\"#DCDCDC\">");
        out.println("<center>");

        File databaseFile = new File("../webapps/banking/logs/" + username + ".log");
        if (databaseFile.length() == 0) {
            out.println("<h3 class='error'>History cannot be found for " + username + ". Try another time?");
        } else {
            out.println("<h3 class='success'>" + username + "'s History:</h3>");
            try {
                Scanner myReader = new Scanner(databaseFile);
                while (myReader.hasNextLine()) {
                  String data = myReader.nextLine();
                  out.println(data + "<br />");
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                out.println("An error occurred... Try another time?");
            }
        }

        out.println("</center>");
        out.println("</body>");
        out.println("<style>.error { color: red; } .success { color: #6cc070; }</style>");
        out.println("</html>");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }
}
