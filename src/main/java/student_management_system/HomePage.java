package student_management_system;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/homepage")
public class HomePage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static String escapeHtml(Object value) {
        if (value == null) return "";
        return value.toString().replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#x27;");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setHeader("Cache-Control", "no-store");
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("id") == null) {
            resp.sendRedirect("login.html");
            return;
        }

        PrintWriter out = resp.getWriter();
        out.println("<!doctype html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Student Management System</title></head><body>");
        out.println("<h1>Welcome to Student Management System</h1>");
        out.println("<h2>" + escapeHtml(session.getAttribute("name")) + "</h2>");
        out.println("<nav><ul>");
        out.println("<li><a href=\"profile\">Profile</a></li>");
        out.println("<li><a href=\"addStudent.html\">Add Student</a></li>");
        out.println("<li><a href=\"findStudent.html\">Find Student</a></li>");
        out.println("<li><a href=\"deleteStudent.html\">Remove Student</a></li>");
        out.println("<li><a href=\"updateStudent.html\">Update Student Details</a></li>");
        out.println("</ul></nav>");
        out.println("<form action=\"logout\" method=\"post\"><button type=\"submit\">Logout</button></form>");
        out.println("</body></html>");
    }
}
