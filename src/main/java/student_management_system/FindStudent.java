package student_management_system;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/find")
public class FindStudent extends HttpServlet {
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

        final int id;
        try {
            id = Integer.parseInt(req.getParameter("id"));
            if (id <= 0) throw new NumberFormatException("ID must be positive");
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "A valid positive student ID is required.");
            return;
        }

        String url = System.getenv().getOrDefault("DB_URL",
                "jdbc:mysql://localhost:3306/student_management_system");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");
        if (dbUser == null || dbPassword == null) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Database configuration is missing. Set DB_USER and DB_PASSWORD.");
            return;
        }

        String sql = "SELECT id, name, email, number, role FROM users WHERE id = ?";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(url, dbUser, dbPassword);
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                try (ResultSet result = statement.executeQuery()) {
                    PrintWriter out = resp.getWriter();
                    out.println("<!doctype html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Find Student</title></head><body>");
                    if (!result.next()) {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.println("<h3>Student does not exist.</h3>");
                    } else {
                        out.println("<h1>Student details</h1>");
                        out.println("<p>ID: " + result.getInt("id") + "</p>");
                        out.println("<p>Name: " + escapeHtml(result.getString("name")) + "</p>");
                        out.println("<p>Email: " + escapeHtml(result.getString("email")) + "</p>");
                        out.println("<p>Number: " + result.getLong("number") + "</p>");
                        out.println("<p>Role: " + escapeHtml(result.getString("role")) + "</p>");
                    }
                    out.println("<p><a href=\"findStudent.html\">Search again</a></p>");
                    out.println("<p><a href=\"homepage\">Home</a></p></body></html>");
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            getServletContext().log("Student lookup failed.", e);
            if (!resp.isCommitted()) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Unable to look up the student right now.");
            }
        }
    }
}
