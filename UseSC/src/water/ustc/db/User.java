package water.ustc.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class User {

	private static final String URL = "jdbc:mysql://localhost:3306/tracing?serverTimezone=GMT%2B8&useSSL=false";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "Jellal20143647";
	
	private String id;
	private String psw;
	
	public User(String id, String psw) {
		this.id = id;
		this.psw = psw;
		
		//连接数据库
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Success loading MySql Driver!");
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error loading MySql Driver!");
			e.printStackTrace();
		}
	}
	
	//login
	public boolean login() {
		System.out.println("Call User.login	id:" + id + "	password:" + psw);
		String password = "";
		int count = 0;
		try {
			Connection c = DriverManager.getConnection(
					this.URL, this.USERNAME, this.PASSWORD);
			Statement st = c.createStatement();
			//数据库中查找用户
			String sql = "select password from user "
					+ "where username='" + id + "'";
			System.out.println("sql:" + sql);
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				password = rs.getString("password");
				count++;
			}
			rs.close();
			st.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		//比较数据库中密码与用户输入的密码是否匹配
		boolean f;
		System.out.println("User.login back! " + (f = (count == 1) && password.equals(psw)));
		return f;
	}
	
	//regist
	public boolean regist() {
		System.out.println("Call User.regist	id:" + id + "	password:" + psw);
		boolean f = true;
		try {
			Connection c = DriverManager.getConnection(
					this.URL, this.USERNAME, this.PASSWORD);
			//插入数据
			String sql = "insert into tracing.user values(?,?)";
			System.out.println("sql:" + sql);
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, psw);
			ps.executeUpdate();
			ps.close();
			c.close();
		}catch (Exception e) {
			// TODO: handle exception
			//插入失败说明用户已存在
			f = false;
		}
		System.out.println("User.regist back! " + f);
		return f;
	}
	
//	public static void main(String[] args) {
//		System.out.println(new User("admin", "adimnps_w_").login());
//	}
	
}
