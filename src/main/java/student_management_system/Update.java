package student_management_system;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/update")
public class Update extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Use POST to update your profile.");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Cache-Control", "no-store");
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("id") == null) {
            resp.sendRedirect("login.html");
            return;
        }

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String numberText = req.getParameter("number");
        String role = req.getParameter("role");
        if (isBlank(name) || isBlank(email) || isBlank(numberText) || isBlank(role)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Name, email, number, and role are required.");
            return;
        }

        final long number;
        try {
            number = Long.parseLong(numberText);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Number must contain digits only.");
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

        String sql = "UPDATE users SET name = ?, email = ?, number = ?, role = ? WHERE id = ?";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(url, dbUser, dbPassword);
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name.trim());
                statement.setString(2, email.trim());
                statement.setLong(3, number);
                statement.setString(4, role.trim());
                statement.setInt(5, ((Number) session.getAttribute("id")).intValue());

                if (statement.executeUpdate() != 1) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Profile not found.");
                    return;
                }
            }
            session.setAttribute("name", name.trim());
            session.setAttribute("email", email.trim());
            session.setAttribute("number", number);
            session.setAttribute("role", role.trim());
            resp.sendRedirect("profile");
        } catch (ClassNotFoundException | SQLException e) {
            getServletContext().log("Profile update failed.", e);
            if (!resp.isCommitted()) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Unable to update profile right now.");
            }
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
