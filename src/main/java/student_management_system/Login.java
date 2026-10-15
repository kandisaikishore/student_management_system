package student_management_system;

import java.io.IOException;
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

@WebServlet("/login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("login.html");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Cache-Control", "no-store");
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            resp.sendRedirect("login.html?error=invalid");
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

        int id;
        String name;
        String storedEmail;
        String storedPassword;
        long number;
        String role;
        String sql = "SELECT id, name, email, password, number, role FROM users WHERE email = ?";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(url, dbUser, dbPassword);
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, email.trim());
                try (ResultSet result = statement.executeQuery()) {
                    if (!result.next()) {
                        resp.sendRedirect("login.html?error=invalid");
                        return;
                    }
                    id = result.getInt("id");
                    name = result.getString("name");
                    storedEmail = result.getString("email");
                    storedPassword = result.getString("password");
                    number = result.getLong("number");
                    role = result.getString("role");
                }
            }

            if (!PasswordUtil.verify(password, storedPassword)) {
                resp.sendRedirect("login.html?error=invalid");
                return;
            }

            // Migrate legacy plain-text passwords after a successful login.
            if (!PasswordUtil.isHashed(storedPassword)) {
                try (Connection connection = DriverManager.getConnection(url, dbUser, dbPassword);
                     PreparedStatement update = connection.prepareStatement(
                             "UPDATE users SET password = ? WHERE id = ? AND password = ?")) {
                    update.setString(1, PasswordUtil.hash(password));
                    update.setInt(2, id);
                    update.setString(3, storedPassword);
                    update.executeUpdate();
                }
            }

            HttpSession previous = req.getSession(false);
            if (previous != null) {
                previous.invalidate();
            }
            HttpSession session = req.getSession(true);
            session.setAttribute("id", id);
            session.setAttribute("name", name);
            session.setAttribute("email", storedEmail);
            session.setAttribute("number", number);
            session.setAttribute("role", role);
            resp.sendRedirect("homepage");
        } catch (ClassNotFoundException | SQLException e) {
            getServletContext().log("Login failed due to a server/database error.", e);
            if (!resp.isCommitted()) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Unable to process login right now.");
            }
        }
    }
}
