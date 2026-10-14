package student_management_system;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@WebServlet("/profile")
public class Profile extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session=req.getSession();;
		int id=(int)session.getAttribute("id");
		String name=(String)session.getAttribute("name");
		String email=(String)session.getAttribute("email");
		String password=(String)session.getAttribute("password");
		long number=(long)session.getAttribute("number");
		String role=(String)session.getAttribute("role");
		
		PrintWriter out=resp.getWriter();
		out.println("<html>");
		out.println("<body>");
			out.println("<form action='update' method='post'>");
				out.println("<input type='hidden' name='id' value='"+id+"'><br>");
				out.println("Name: <input type='text' name='name' value='"+name+"'><br>");
				out.println("Email: <input type='email' name='email' value='"+email+"'><br>");
				out.println("Password: <input type='password' name='password' value='"+password+"'><br>");
				out.println("Number: <input type='tel' name='number' value='"+number+"'><br>");
				out.println("Role: <input type='text' name='role' value='"+role+"'><br>");
				out.println("<a href='update'><button>Update</button></a>");				
			out.println("</form>");
			out.println("<a href='delete?id="+id+"'><button>delete</button></a>");				
		out.println("</body>");
		out.println("</html>");
	}
}