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

@WebServlet("/updateStudent")
public class UpdateStudent extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Use POST to update a student record.");
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

        final int id;
        final long number;
        String idText = req.getParameter("id");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String numberText = req.getParameter("number");
        String role = req.getParameter("role");
        if (isBlank(idText) || isBlank(name) || isBlank(email) || isBlank(password)
                || isBlank(numberText) || isBlank(role)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "All fields are required.");
            return;
        }
        if (password.length() < 8) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Password must contain at least 8 characters.");
            return;
        }
        try {
            id = Integer.parseInt(idText);
            number = Long.parseLong(numberText);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID and phone number must be numeric.");
            return;
        }
        int authenticatedId = ((Number) session.getAttribute("id")).intValue();
        if (id != authenticatedId) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You can update only your own account.");
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

        String sql = "UPDATE users SET name = ?, email = ?, password = ?, number = ?, role = ? WHERE id = ?";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(url, dbUser, dbPassword);
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name.trim());
                statement.setString(2, email.trim());
                statement.setString(3, PasswordUtil.hash(password));
                statement.setLong(4, number);
                statement.setString(5, role.trim());
                statement.setInt(6, authenticatedId);
                if (statement.executeUpdate() != 1) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Account not found.");
                    return;
                }
            }
            session.setAttribute("name", name.trim());
            session.setAttribute("email", email.trim());
            session.setAttribute("number", number);
            session.setAttribute("role", role.trim());
            resp.sendRedirect("homepage");
        } catch (ClassNotFoundException e) {
            getServletContext().log("MySQL JDBC driver is unavailable.", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database driver is unavailable.");
        } catch (SQLException e) {
            getServletContext().log("Unable to update student account.", e);
            if ("23000".equals(e.getSQLState())) {
                resp.sendError(HttpServletResponse.SC_CONFLICT, "That email is already registered.");
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Unable to update the account right now.");
            }
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
