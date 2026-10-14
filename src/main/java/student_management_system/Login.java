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
import javax.servlet.http.HttpSession;
@WebServlet("/login")
public class Login extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email=req.getParameter("email");
		String password=req.getParameter("password");
		PrintWriter out=resp.getWriter();
//		System.out.println(email);
//		System.out.println(password);
		
		out.println("<html>");
		out.println("<body>");
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			String url="jdbc:mysql://localhost:3306/student_management_system?user=root&password=root";
			connection=DriverManager.getConnection(url);
			String sql="SELECT * FROM users WHERE email=?";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setString(1, email);
			resultSet=preparedStatement.executeQuery();
			if(resultSet.next()) {
				String dbPwd=resultSet.getString("password");
				if(password.equals(dbPwd)) {
//					RequestDispatcher dispatcher=req.getRequestDispatcher("homepage");
//					dispatcher.forward(req, resp);
					int id=resultSet.getInt("id");
					String name=resultSet.getString("name");
					String role=resultSet.getString("role");
					long number=resultSet.getLong("number");
					
					HttpSession session=req.getSession();
					session.setAttribute("id", id);
					session.setAttribute("name", name);
					session.setAttribute("email", email);
					session.setAttribute("password", password);
					session.setAttribute("number", number);
					session.setAttribute("role", role);
					
					resp.sendRedirect("homepage");//creates new request and response object.
//					resp.sendRedirect("https://www.google.com");   //we can redirect the servlet to pages not in servlet folder.
				}else {
					RequestDispatcher dispatcher=req.getRequestDispatcher("login.html");
					out.println("<h1>Invalid Password</h1>");
					dispatcher.include(req, resp);
				}
			}else {
				RequestDispatcher dispatcher=req.getRequestDispatcher("login.html");
				out.println("<h1>Invalid Email</h1>");
				dispatcher.include(req, resp);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				if(resultSet!=null)
					resultSet.close();
				if(preparedStatement!=null)
					preparedStatement.close();
				if(connection!=null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		out.println("</body>");
		out.println("</html>");
	}
}