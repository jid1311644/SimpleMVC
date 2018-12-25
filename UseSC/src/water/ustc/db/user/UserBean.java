package water.ustc.db.user;

import java.util.ArrayList;

import com.mysql.cj.jdbc.Driver;

import net.sf.cglib.proxy.Enhancer;
import sc.ustc.tools.LazyLoadProxy;
import water.ustc.db.score.ScoreBean;
import water.ustc.db.user.UserDAO;

public class UserBean {
	
	private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/j2ee_expiration_data?serverTimezone=GMT%2B8&useSSL=false";
	private static final String MYSQL_USERNAME = "root";
	private static final String MYSQL_PASSWORD = "Jellal20143647";
	
	private static final String SQLITE_DRIVER = "org.sqlite.JDBC";
	private static final String SQLITE_URL = "jdbc:sqlite:D:/SQLite/j2ee_expiration_data.db";
	private static final String SQLITE_USERNAME = "";
	private static final String SQLITE_PASSWORD = "";
	
	private UserDAO userDAO;
	private ScoreBean scoreBean;
	
	private String userId;
	private String userName;
	private String userPass;
	
	public UserBean() {
		// TODO Auto-generated constructor stub
		System.out.println("This is constructor UserBean().");
	}
	
	public UserBean(String userId, String userName, String userPass) {
		this.userId = userId;
		this.userName = userName;
		this.userPass = userPass;
	}
	
	@SuppressWarnings("static-access")
	private LazyUserPassword createUserPass() {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(LazyUserPassword.class);
		return (LazyUserPassword) enhancer.create(
				LazyUserPassword.class, new LazyLoadProxy(this.userId));
	}
	/*
	//登录时需要select password，延迟加载
	public boolean signIn() {
		System.out.println("Call UserBean.signIn with lazy-loading\r\n"
				+ "	id:" + userId + "	password:" + userPass);
		LazyUserPassword psw = createUserPass();
		if(psw == null) {
			return false;
		}
		else {
			if(psw.getUserPassword().equals(userPass)) {
				return true;
			}
			else {
				return false;
			}
		}
	}*/
	
	//通过di.xml配置文件，Java内省机制实现登录
	public boolean signIn() {
		System.out.println("Call UserBean.signIn"
				+ "	id:" + userId + "	password:" + userPass);
		ArrayList<UserBean> beans = userDAO.query(
				new UserBean(null, null, userPass),
				new UserBean(userId, null, null));
		if(beans.size() != 1) {
			return false;
		}
		else {
			if(beans.get(0).getUserPass().equals(userPass)) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	
/*	//登录
	public boolean signIn() {
		System.out.println("Call UserBean.signIn"
				+ "	id:" + userId + "	password:" + userPass);
		String sql = "select * from user "
				+ "where id='" + userId + "'";
		//使用MySQL
		UserBean userBean = (UserBean) new UserDAO(
				MYSQL_DRIVER, MYSQL_URL, MYSQL_USERNAME, MYSQL_PASSWORD).query(sql);
		//使用SQLite
//		UserBean userBean = (UserBean) new UserDAO(
//				SQLITE_DRIVER, SQLITE_URL, SQLITE_USERNAME, SQLITE_PASSWORD).query(sql);
		if(userBean == null) {
			//用户不存在
			return false;
		}
		else {
			if(userBean.getUserPass().equals(userPass)) {
				return true;
			}
			else {
				//密码错误
				return false;
			}
		}
	}*/
	
	//注册
	public boolean signUp() {
		System.out.println("Call UserBean.signUp"
				+ "	id:" + userId + "	password:" + userPass);
//		String sql = "insert into user values('" + userId + "','" +
//				userPass + "','" + userId + "')";
//		//使用MySQL
//		return new UserDAO(MYSQL_DRIVER, MYSQL_URL, MYSQL_USERNAME, MYSQL_PASSWORD).
//				insert(sql);
//		//使用SQLite
////		return new UserDAO(SQLITE_DRIVER, SQLITE_URL, SQLITE_USERNAME, SQLITE_PASSWORD).
////				insert(sql);
		return new UserDAO().insert(new UserBean(userId, userId, userPass));
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserPass() {
		return userPass;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		System.out.println("Call UserBean.setUserDAO ...");
		this.userDAO = userDAO;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public ScoreBean getScoreBean() {
		return scoreBean;
	}

	public void setScoreBean(ScoreBean scoreBean) {
		this.scoreBean = scoreBean;
	}

}
