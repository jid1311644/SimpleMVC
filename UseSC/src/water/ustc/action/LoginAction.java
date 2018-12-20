package water.ustc.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import water.ustc.db.User;
import water.ustc.user.UserBean;

public class LoginAction extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public String handleLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		String password = request.getParameter("password");
		HttpSession session = request.getSession();
		
		System.out.println("Call handleLogin	id:" + id + "	password:" + password);
		if(new UserBean(id, password).signIn()) {
			session.setAttribute("id", id);
			System.out.println("handleLogin back:OK!");
			return "ok";
		}
		else {
			session.setAttribute("loginMessage", "ID or password error!");
			System.out.println("handleLogin back:ID or password error!");
			return "error";
		}
	}

}
