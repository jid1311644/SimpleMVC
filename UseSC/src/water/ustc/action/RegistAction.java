package water.ustc.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import water.ustc.db.User;

public class RegistAction extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public String handleRegist(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		String password = request.getParameter("password");
		HttpSession session = request.getSession();
		
		if(new User(id, password).regist()) {
			session.setAttribute("id", id);
			return "ok";
		}
		else {
			session.setAttribute("registMessage", "ID already exists!");
			return "error";
		}
	}
	
}
