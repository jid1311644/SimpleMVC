package water.ustc.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import water.ustc.db.user.UserBean;

public class LoginAction extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private UserBean userBean;
	
	public String handleLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		String password = request.getParameter("password");
		HttpSession session = request.getSession();
		
		System.out.println("Call handleLogin	id:" + id + "	password:" + password);
		userBean.setUserId(id);
		userBean.setUserPass(password);
		userBean.setUserName(null);
		if(userBean.signIn()) {
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
	
	public void introTest(String id, String password) {
		userBean.setUserId(id);
		userBean.setUserPass(password);
		userBean.setUserName(null);
	}
	
	public void display() {
		System.out.println(userBean.getUserId() + "	" + userBean.getUserPass());
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		System.out.println("Call LoginAction.setUserBean ...");
		this.userBean = userBean;
	}

}
