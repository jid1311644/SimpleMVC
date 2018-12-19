package water.ustc.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutAction extends HttpServlet {
	
	public void postLogout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Call LogoutAction.postLogout ...");
		PrintWriter out = response.getWriter();
		out.write("<div>Logout success!</div>\r\n");
	}
	
	public void postViewTest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Call LogoutAction.postViewTest ...");
		PrintWriter out = response.getWriter();
		out.write("<div>View Test:</div>\r\n");
		out.write("<div>Username:" + request.getParameter("textFieldTest") + "</div>\r\n");
		out.write("<div>Password:" + request.getParameter("pswFieldTest") + "</div>\r\n");
		out.write("<div>CheckBox1:" + request.getParameter("checkBoxTest1") + "</div>\r\n");
		out.write("<div>CheckBox2:" + request.getParameter("checkBoxTest2") + "</div>\r\n");
		out.write("<div>RadioGroup:" + request.getParameter("radioGroupTest") + "</div>\r\n");
		out.write("<div>ListView:" + request.getParameter("listViewTest") + "</div>\r\n");
	}

}
