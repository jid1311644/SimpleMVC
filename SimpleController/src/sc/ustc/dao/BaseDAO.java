package sc.ustc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public abstract class BaseDAO {
	
	protected String driver; //数据库驱动类
	protected String url;
	protected String userName;
	protected String userPassword;
	
	public BaseDAO(String driver, String url, 
			String userName, String userPassword) {
		// TODO Auto-generated constructor stub
		this.driver = driver;
		this.url = url;
		this.userName = userName;
		this.userPassword = userPassword;
	}
	
	//打开数据库连接
	protected Connection openDBConnection() {
		//连接数据库
		Connection c = null;
		try {
			Class.forName(driver);
			System.out.println("Success loading Driver!");
			if(this.userName.equals("")) {
				//连接的是SQLite数据库
				System.out.println("Connecting SQLite ...");
				c = DriverManager.getConnection(this.url);
			}
			else {
				//连接的是MySQL数据库
				System.out.println("Connecting MySQL ...");
				c = DriverManager.getConnection(
						this.url, this.userName, this.userPassword);
			}
			System.out.println("Success connecting DBMS!");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Error loading DBMS Driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error connecting DBMS!");
			e.printStackTrace();
		}
		return c;

	}
	
	//关闭数据库连接
	protected boolean closeDBConnection(Connection c) {
		if(c == null) {
			return false;
		}
		else {
			try {
				c.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
	}
	
	//执行sql语句并返回结果对象
	protected abstract Object query(String sql);
	//执行插入数据的sql语句并返回执行结果
	protected abstract boolean insert(String sql);
	//执行更新数据的sql语句并返回执行结果
	protected abstract boolean update(String sql);
	//执行删除数据的sql语句并返回执行结果
	protected abstract boolean delete(String sql);
	

}
