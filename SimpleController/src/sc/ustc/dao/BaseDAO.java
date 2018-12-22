package sc.ustc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import sc.ustc.dao.conversate.Conversation;


public abstract class BaseDAO {
	
	protected String driver; //���ݿ�������
	protected String url;
	protected String userName;
	protected String userPassword;
	
	private Conversation conversation;
	
	//ֱ��ʹ�����ݿ�
	public BaseDAO(String driver, String url, 
			String userName, String userPassword) {
		// TODO Auto-generated constructor stub
		this.driver = driver;
		this.url = url;
		this.userName = userName;
		this.userPassword = userPassword;
	}
	
	//ͨ��OR-Mappingʹ�����ݿ�
	public BaseDAO() {
		// TODO Auto-generated constructor stub
		this.conversation = new Conversation();
	}
	
	//�����ݿ�����
	protected Connection openDBConnection() {
		//�������ݿ�
		Connection c = null;
		try {
			Class.forName(driver);
			System.out.println("Success loading Driver!");
			if(this.userName.equals("")) {
				//���ӵ���SQLite���ݿ�
				System.out.println("Connecting SQLite ...");
				c = DriverManager.getConnection(this.url);
			}
			else {
				//���ӵ���MySQL���ݿ�
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
	
	//�ر����ݿ�����
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
	
	//ִ��sql��䲢���ؽ������
	protected abstract Object query(String sql);
	//ִ�в������ݵ�sql��䲢����ִ�н��
	protected abstract boolean insert(String sql);
	//ִ�и������ݵ�sql��䲢����ִ�н��
	protected abstract boolean update(String sql);
	//ִ��ɾ�����ݵ�sql��䲢����ִ�н��
	protected abstract boolean delete(String sql);
	
	//����OR-Mappingʹ�����ݿ�
	//property��Ҫselect�����ԣ�value��select����where�е����Ժ�ֵ
	protected Object queryByOR(Object property, Object value) {
		return conversation.selectObject(property, value);
	}
	//value��Ҫ���������
	protected boolean insertByOR(Object value) {
		return conversation.insertObject(value);
	}
	//property��Ҫ���µ��к���ֵ�� value��where�е����Ժ�ֵ
	protected boolean updateByOR(Object property, Object value) {
		return conversation.updateObject(property, value);
	}
	//����value�е����Ժ���ֵdeleteԪ��
	protected boolean deleteByOR(Object value) {
		return conversation.deleteObject(value);
	}

}
