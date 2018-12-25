package sc.ustc.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.sf.cglib.proxy.LazyLoader;
import sc.ustc.dao.config.result.ConfigJDBC;
import sc.ustc.dao.conversate.Conversation;
import water.ustc.db.user.LazyUserPassword;

public class LazyLoadProxy implements LazyLoader {
	
	private String userId;
	
	public LazyLoadProxy(String id) {
		// TODO Auto-generated constructor stub
		this.userId = id;
	}

	@Override
	public Object loadObject() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Call LazyLoadProxy.loadObject ...");
		ConfigJDBC jdbc = new Conversation().getConfigResult().getJdbc();
		Class.forName(jdbc.getDriver());
		
		//MySQL
		Connection c = DriverManager.getConnection(jdbc.getUrl() + "&allowPublicKeyRetrieval=true", 
				jdbc.getDbUserName(), jdbc.getDbUserPassword());
		//SQLite
//		Connection c = DriverManager.getConnection(jdbc.getUrl());
		String sql = "select password from user where id='" + userId + "'";
		System.out.println("LazyLoadProcy SQL:" + sql);
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		LazyUserPassword psw = null;
		while(rs.next()) {
			psw = new LazyUserPassword();
			psw.setUserPassword(rs.getString("password"));
		}
		return psw;
	}
	
	

}
