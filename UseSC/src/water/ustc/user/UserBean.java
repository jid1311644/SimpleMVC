package water.ustc.user;

import com.mysql.cj.jdbc.Driver;

import water.ustc.user.UserDAO;

public class UserBean {
	
	private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/j2ee_expiration_data?serverTimezone=GMT%2B8&useSSL=false";
	private static final String MYSQL_USERNAME = "root";
	private static final String MYSQL_PASSWORD = "Jellal20143647";
	
	private static final String SQLITE_DRIVER = "org.sqlite.JDBC";
	private static final String SQLITE_URL = "jdbc:sqlite:D:/SQLite/j2ee_expiration_data.db";
	private static final String SQLITE_USERNAME = "";
	private static final String SQLITE_PASSWORD = "";
	
	private String userId;
	private String userName;
	private String userPass;
	
	public UserBean(String userId, String userPass) {
		this.userId = userId;
		this.userPass = userPass;
		this.userName = userId;
	}
	
	//登录
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
	}
	
	//注册
	public boolean signUp() {
		System.out.println("Call UserBean.signUp"
				+ "	id:" + userId + "	password:" + userPass);
		String sql = "insert into user values('" + userId + "','" +
				userPass + "','" + userName + "')";
		//使用MySQL
		return new UserDAO(MYSQL_DRIVER, MYSQL_URL, MYSQL_USERNAME, MYSQL_PASSWORD).
				insert(sql);
		//使用SQLite
//		return new UserDAO(SQLITE_DRIVER, SQLITE_URL, SQLITE_USERNAME, SQLITE_PASSWORD).
//				insert(sql);
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

}
