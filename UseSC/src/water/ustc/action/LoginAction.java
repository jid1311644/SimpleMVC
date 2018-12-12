package water.ustc.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import water.ustc.db.User;

public class LoginAction extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public String handleLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		String password = request.getParameter("password");
		HttpSession session = request.getSession();
		
		System.out.println("Call handleLogin	id:" + id + "	password:" + password);
		if(new User(id, password).login()) {
			session.setAttribute("id", id);
			System.out.println("handleLogin back!");
			return "ok";
		}
		else {
			session.setAttribute("loginMessage", "ID or password error!");
			System.out.println("handleLogin back!");
			return "error";
		}
	}

}
