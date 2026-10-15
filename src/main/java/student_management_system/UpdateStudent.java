package student_management_system;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/updateStudent")
public class UpdateStudent extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id=Integer.parseInt(req.getParameter("id"));
		String name=req.getParameter("name");
		String email=req.getParameter("email");
		String password=req.getParameter("password");
		long number=Long.parseLong(req.getParameter("number"));
		String role=req.getParameter("role");
		
		Connection connection=null;
		PreparedStatement statement=null,stmt=null;
		ResultSet set=null;
		PrintWriter out=resp.getWriter();
		out.println("<html>");
		out.println("<body>");
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url=System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/student_management_system");
			String dbUser=System.getenv("DB_USER");
			String dbPassword=System.getenv("DB_PASSWORD");
			if(dbUser==null || dbPassword==null) {
				throw new SQLException("DB_USER and DB_PASSWORD must be configured");
			}
			connection=DriverManager.getConnection(url, dbUser, dbPassword);
			
			String sql="SELECT * FROM users WHERE id=?";
			stmt=connection.prepareStatement(sql);
			stmt.setInt(1, id);
			set=stmt.executeQuery();
			if(set.next()) {
			String update="UPDATE users SET name=?,email=?,password=?,number=?,role=? WHERE id=?";
			statement=connection.prepareStatement(update);
			statement.setString(1, name);
			statement.setString(2, email);
			statement.setString(3, password);
			statement.setLong(4, number);
			statement.setString(5, role);
			statement.setInt(6, id);
			statement.executeUpdate();
			resp.sendRedirect("homepage");
			}else {
				RequestDispatcher dispatcher=req.getRequestDispatcher("updateStudent.html");
				out.println("<h3>Student does not Exist</h3>");
				dispatcher.include(req, resp);
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if(set!=null)
					set.close();
				if(stmt!=null)
					stmt.close();
				if(statement!=null)
					statement.close();
				if(connection!=null)
					connection.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		out.println("</body>");
		out.println("</html>");
	}
}