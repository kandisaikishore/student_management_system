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
@WebServlet("/addStudent")
public class AddStudent extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		String adminName=req.getParameter("adminName");
//		int adminId=Integer.parseInt(req.getParameter("adminId"));
		int id=Integer.parseInt(req.getParameter("id"));
		String name=req.getParameter("name");
		String email=req.getParameter("email");
		String pwd=req.getParameter("password");
		long phNo=Long.parseLong(req.getParameter("number"));
		String role=req.getParameter("role");
		PrintWriter out=resp.getWriter();
		
		out.println("<html>");
		out.println("<body>");
		Connection connection=null;
		PreparedStatement statement=null,stmt=null;
		ResultSet set=null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			String url=System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/student_management_system");
			String dbUser=System.getenv("DB_USER");
			String dbPassword=System.getenv("DB_PASSWORD");
			if(dbUser==null || dbPassword==null) {
				throw new SQLException("DB_USER and DB_PASSWORD must be configured");
			}
			connection=DriverManager.getConnection(url, dbUser, dbPassword);
			String sql="INSERT INTO users VALUES(?,?,?,?,?,?)";
			statement=connection.prepareStatement(sql);
			statement.setInt(1,id);
			statement.setString(2, name);
			statement.setString(3, email);
			statement.setString(4, pwd);
			statement.setLong(5, phNo);
			statement.setString(6, role);
			String sql2="SELECT * FROM users WHERE id=?";
			stmt=connection.prepareStatement(sql2);
			stmt.setInt(1, id);
			set=stmt.executeQuery();
			if(set.next()) {
				RequestDispatcher disp=req.getRequestDispatcher("addStudent.html");
				out.println("<h1>User Already Exists</h1>");
				disp.include(req, resp);
			}else {
				statement.execute();
				set=stmt.executeQuery();
				if(set.next()) {
					if(set.getString(2).equals(name) && set.getString(3).equals(email))
						resp.sendRedirect("homepage");
				}else {
					RequestDispatcher disp=req.getRequestDispatcher("addStudent.html");
					out.println("<h1>Registration failed</h1>");
					disp.include(req, resp);
					
				}
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(connection!=null)
					connection.close();
				if(statement!=null)
					statement.close();
				if(stmt!=null)
					stmt.close();
				if(set!=null)
					set.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		out.println("</body>");
		out.println("</html>");
	}
}