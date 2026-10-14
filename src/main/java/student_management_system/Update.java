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
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id=Integer.parseInt(req.getParameter("id"));
		String name=req.getParameter("name");
		String email=req.getParameter("email");
		String password=req.getParameter("password");
		long number=Long.parseLong(req.getParameter("number"));
		String role=req.getParameter("role");
		
		Connection connection=null;
		PreparedStatement statement=null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url="jdbc:mysql://localhost:3306/student_management_system?user=root&password=root";
			connection=DriverManager.getConnection(url);
			String sql="UPDATE users SET name=?,email=?,password=?,number=?,role=? WHERE id=?";
			statement=connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.setString(2, email);
			statement.setString(3, password);
			statement.setLong(4, number);
			statement.setString(5, role);
			statement.setInt(6, id);
			statement.executeUpdate();
			HttpSession session=req.getSession();
			session.setAttribute("name", name);
			session.setAttribute("email", email);
			session.setAttribute("password", password);
			session.setAttribute("number", number);
			session.setAttribute("role", role);
			resp.sendRedirect("profile");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if(statement!=null)
					statement.close();
				if(connection!=null)
					connection.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

}