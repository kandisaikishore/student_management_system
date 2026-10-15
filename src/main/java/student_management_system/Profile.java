package student_management_system;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/profile")
public class Profile extends HttpServlet {
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

        String id = escapeHtml(session.getAttribute("id"));
        String name = escapeHtml(session.getAttribute("name"));
        String email = escapeHtml(session.getAttribute("email"));
        String number = escapeHtml(session.getAttribute("number"));
        String role = escapeHtml(session.getAttribute("role"));

        PrintWriter out = resp.getWriter();
        out.println("<!doctype html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Profile</title></head><body>");
        out.println("<h1>Profile</h1>");
        out.println("<form action=\"update\" method=\"post\">");
        out.println("<label>Name <input type=\"text\" name=\"name\" value=\"" + name + "\" required></label><br>");
        out.println("<label>Email <input type=\"email\" name=\"email\" value=\"" + email + "\" required></label><br>");
        out.println("<label>Number <input type=\"tel\" name=\"number\" value=\"" + number + "\" required></label><br>");
        out.println("<label>Role <input type=\"text\" name=\"role\" value=\"" + role + "\" required></label><br>");
        out.println("<button type=\"submit\">Update profile</button></form>");
        out.println("<p><a href=\"homepage\">Back to home</a></p></body></html>");
    }
}
