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

@WebServlet("/find")
public class FindStudent extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id=Integer.parseInt(req.getParameter("id"));
		
		Connection connection=null;
		PreparedStatement statement=null;
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
			statement=connection.prepareStatement(sql);
			statement.setInt(1, id);
			set=statement.executeQuery();
			if(set.next()) {
				int i=set.getInt(1);
				String name=set.getString(2);
				String email=set.getString(3);
				long num=set.getLong(5);
				String rol=set.getString(6);
				out.println("<h3>ID:"+id+", Name:"+name+", email:"+email+", Number:"+num+" Role:"+rol+"</h3><br>");
				out.println("<a href='homepage'><button>Home</button></a>");
			}
			else {
				
				RequestDispatcher dispatcher=req.getRequestDispatcher("findStudent.html");
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