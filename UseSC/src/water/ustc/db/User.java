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
		
		//�������ݿ�
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
		String password = "";
		try {
			Connection c = DriverManager.getConnection(
					this.URL, this.USERNAME, this.PASSWORD);
			Statement st = c.createStatement();
			//���ݿ��в����û�
			String sql = "select password from user "
					+ "where username='" + id + "'";
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				password = rs.getString("password");
			}
			rs.close();
			st.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		//�Ƚ����ݿ����������û�����������Ƿ�ƥ��
		return password.equals(psw);
	}
	
	//regist
	public boolean regist() {
		boolean f = true;
		try {
			Connection c = DriverManager.getConnection(
					this.URL, this.USERNAME, this.PASSWORD);
			//��������
			String sql = "insert into tracing.user values(?,?)";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, psw);
			ps.executeUpdate();
			ps.close();
			c.close();
		}catch (Exception e) {
			// TODO: handle exception
			//����ʧ��˵���û��Ѵ���
			f = false;
		}
		return f;
	}
	
//	public static void main(String[] args) {
//		System.out.println(new User("admin", "adimnps_w_").login());
//	}
	
}
