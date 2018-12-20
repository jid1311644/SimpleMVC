package water.ustc.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sc.ustc.dao.BaseDAO;

public class UserDAO extends BaseDAO {
	
	public UserDAO(String driver, String url, 
			String userName, String userPassword) {
		super(driver, url, userName, userPassword);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean delete(String sql) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean insert(String sql) {
		// TODO Auto-generated method stub
		System.out.println("Call UserDAO.insert ...");
		System.out.println("sql:" + sql);
		boolean f = true;
		try {
			//建立数据库链接
			Connection c = openDBConnection();
			//插入数据
			PreparedStatement ps = c.prepareStatement(sql);
			ps.executeUpdate();
			ps.close();
			//关闭数据库链接
			closeDBConnection(c);
		}catch (Exception e) {
			// TODO: handle exception
			//插入失败说明用户已存在
			f = false;
		}
		return f;
	}

	@Override
	protected Object query(String sql) {
		System.out.println("Call UserDAO.query ...");
		System.out.println("sql:" + sql);
		// TODO Auto-generated method stub
		UserBean userBean = null;
		String userId = "";
		String userName = "";
		String password = "";
		int count = 0;
		try {
			//建立数据库链接
			Connection c = openDBConnection();
			Statement st = c.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				userId = rs.getString("id");
				userName = rs.getString("username");
				password = rs.getString("password");
				count++;
			}
			if(count == 1) {
				//若找到用户则为userBean对象初始化赋值
				userBean = new UserBean(userId, password);
				userBean.setUserName(userName);
			}
			rs.close();
			st.close();
			//关闭数据库链接
			closeDBConnection(c);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userBean;
	}

	@Override
	protected boolean update(String sql) {
		// TODO Auto-generated method stub
		return false;
	}

}
