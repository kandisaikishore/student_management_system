package student_management_system;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/signup")
public class Signup extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id=req.getParameter("id");
		String name=req.getParameter("name");
		String email=req.getParameter("email");
		String password=req.getParameter("password");
		String number=req.getParameter("number");
		String role=req.getParameter("role");
		
		PrintWriter out=resp.getWriter();
		
		out.println("<html>");
		out.println("<body>");
		
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		
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
			preparedStatement=connection.prepareStatement(sql);
			
			preparedStatement.setInt(1, Integer.parseInt(id));
			preparedStatement.setString(2, name);
			preparedStatement.setString(3, email);
			preparedStatement.setString(4, password);
			preparedStatement.setLong(5, Long.parseLong(number));
			preparedStatement.setString(6, role);
			
			if(preparedStatement.execute()) {
				out.println("<h1>Signup Failed</h1>");
			}else {
				out.println("<h1>Signup Successful</h1>");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
				try {
					if(preparedStatement!=null)
						preparedStatement.close();
					if(connection!=null)
						connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		out.println("</body>");
		out.println("</html>");
	}
}