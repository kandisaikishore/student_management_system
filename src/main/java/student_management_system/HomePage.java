package student_management_system;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@WebServlet("/homepage")
public class HomePage extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session=req.getSession();
//		int id=Integer.parseInt(req.getParameter("id"));
		String name=(String)session.getAttribute("name");
//		String role=req.getParameter("role");
//		long number=Long.parseLong(req.getParameter("number"));
		
//		System.out.println(req.getParameter("email"));
		PrintWriter out=resp.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<h1>Welcome to HomePage</h1>");
//		out.println("<h4> ID: "+id+"</h4>");
		out.println("<h4> Name: "+name+"</h4>");
//		out.println("<h4> Role: "+role+"</h4>");
//		out.println("<h4> Number: "+number+"</h4>");
		out.println("<a href='profile'><button>Profile</button></a>");
		out.println("<a href='addStudent.html'><button>Add Student</button></a>");
		out.println("<a href='findStudent.html'><button>Find Student</button></a>");
		out.println("<a href='deleteStudent.html'><button>Remove Student</button></a>");
		out.println("<a href='updateStudent.html'><button>Update Student Details</button></a>");
		out.println("<a href='login.html'><button>Logout</button></a>");
		
		out.println("</body>");
		out.println("</html>");
	}
}