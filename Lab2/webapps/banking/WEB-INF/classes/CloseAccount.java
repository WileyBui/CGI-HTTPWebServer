import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CloseAccount extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter  out     = response.getWriter();
        HttpSession session = request.getSession(false);
        List<String> errors  = new ArrayList<>();

        String userName = (String)session.getAttribute("username");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        out.println("<!DOCTYPE html><html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\" />");
        out.println("<title>Sign Up: " + ((errors.size() > 0)? "Error" : "Success") + "!</title>");
        out.println("</head>");
        out.println("<body bgcolor=\"#DCDCDC\">");
        
        
        out.println("alert(");
        out.println("USERNAME: " + userName);

     
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
