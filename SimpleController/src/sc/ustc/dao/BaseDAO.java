package sc.ustc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import sc.ustc.dao.conversate.Conversation;


public abstract class BaseDAO {
	
	protected String driver; //数据库驱动类
	protected String url;
	protected String userName;
	protected String userPassword;
	
	private Conversation conversation;
	
	//直接使用数据库
	public BaseDAO(String driver, String url, 
			String userName, String userPassword) {
		// TODO Auto-generated constructor stub
		this.driver = driver;
		this.url = url;
		this.userName = userName;
		this.userPassword = userPassword;
	}
	
	//通过OR-Mapping使用数据库
	public BaseDAO() {
		// TODO Auto-generated constructor stub
		this.conversation = new Conversation();
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
	
	//根据OR-Mapping使用数据库
	//property是要select的属性，value是select条件where中的属性和值
	protected Object queryByOR(Object property, Object value) {
		return conversation.selectObject(property, value);
	}
	//value是要插入的数据
	protected boolean insertByOR(Object value) {
		return conversation.insertObject(value);
	}
	//property是要更新的列和新值， value是where中的属性和值
	protected boolean updateByOR(Object property, Object value) {
		return conversation.updateObject(property, value);
	}
	//根据value中的属性和其值delete元组
	protected boolean deleteByOR(Object value) {
		return conversation.deleteObject(value);
	}

}
