package water.ustc.db.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import sc.ustc.dao.BaseDAO;

public class UserDAO extends BaseDAO {
	
	public UserDAO(String driver, String url, 
			String userName, String userPassword) {
		super(driver, url, userName, userPassword);
		// TODO Auto-generated constructor stub
	}
	
	public UserDAO() {
		// TODO Auto-generated constructor stub
		System.out.println("This is constructor UserDAO().");
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
			System.out.println("Error executing sql.");
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
				userBean = new UserBean(userId, userName, password);
			}
			rs.close();
			st.close();
			//关闭数据库链接
			closeDBConnection(c);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error executing sql.");
			e.printStackTrace();
		}
		return userBean;
	}

	@Override
	protected boolean update(String sql) {
		// TODO Auto-generated method stub
		return false;
	}
	
	//根据OR-Mapping使用数据库
	//property是要select的属性，value是select条件where中的属性和值
	protected ArrayList<UserBean> query(UserBean property, UserBean value) {
		ArrayList<UserBean> userBeans = new ArrayList<>();
		ResultSet rs = (ResultSet) queryByOR(property, value);
		boolean selectUserId = property.getUserId() != null;
		boolean selectUserName = property.getUserName() != null;
		boolean selectUserPass = property.getUserPass() != null;
		System.out.println("This is UserDAO.query " + selectUserId + selectUserName + selectUserPass);
		String userId = null;
		String userName = null;
		String userPass = null;
		try {
			while(rs.next()) {
				if(selectUserId) {
					userId = rs.getString("id");
				}
				if(selectUserName) {
					userName = rs.getString("username");
				}
				if(selectUserPass) {
					userPass = rs.getString("password");
				}
				userBeans.add(new UserBean(userId, userName, userPass));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userBeans;
	}
	//value是要插入的数据
	protected boolean insert(UserBean value) {
		return insertByOR(value);
	}
	//property是要更新的列和新值， value是where中的属性和值
	protected boolean update(UserBean property, UserBean value) {
		return update(property, value);
	}
	//根据value中的属性和其值delete元组
	protected boolean delete(UserBean value) {
		return delete(value);
	}

}
