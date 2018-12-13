package water.ustc.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class Log {
	
	private String name;
	private String startTime;
	private String endTime;
	private String result;
	
	public Log(String name, String startTime, String endTime, String result) {
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.result = result;
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
	
	public Log(String id, String name, String startTime, String endTime, String result) {
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.result = result;
	}
	
	//add
	public boolean add(String id) {
		boolean f = true;
		System.out.println("Call Log.add ...");
		try {
			Connection c = DriverManager.getConnection(
					User.URL, User.USERNAME, User.PASSWORD);
			//插入数据
			String sql = "insert into j2ee_expiration_data.log values(?,?,?,?,?)";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, name);
			ps.setString(3, startTime);
			ps.setString(4, endTime);
			ps.setString(5, result);
			ps.executeUpdate();
			ps.close();
			c.close();
		}catch (Exception e) {
			// TODO: handle exception
			f = false;
			e.printStackTrace();
		}
		return f;
	}
	
	//save
	public boolean save(String id){
		LinkedList<Log> logs = new LinkedList<>();
		try {
			Connection c = DriverManager.getConnection(
						User.URL, User.USERNAME, User.PASSWORD);

			Statement st = c.createStatement();
			//数据库中读取Log
			String sql = "select * from log "
					+ "where username='" + id + "'";
			System.out.println("sql:" + sql);
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				logs.add(new Log(id, rs.getString("action_name"), 
						rs.getString("start_time"), 
						rs.getString("end_time"), 
						rs.getString("result")));
			}
			rs.close();
			st.close();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		if(logs.isEmpty()) {
			return false;
		}
		
		//编写.xml文件
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
				+ "<log>\r\n");
		while(!logs.isEmpty()) {
			sb.append("	<action>\r\n"
					+ "		<name>" + logs.peekFirst().name + "</name>\r\n"
					+ "		<s-time>" + logs.peekFirst().startTime + "</s-time>\r\n"
					+ "		<e-time>" + logs.peekFirst().endTime + "</e-time>\r\n"
					+ "		<result>" + logs.pollFirst().result + "</result>\r\n"
					+ "	</action>\r\n"
					);
		}
		sb.append("	<!-- other actions -->\r\n</log>\r\n");
		
		try {
			//保存.xml文件
			File file = new File("./logs/log_" + id + ".xml");
			if(!file.exists()) {
				file.delete();
			}
			file.createNewFile();
			PrintWriter pw = new PrintWriter(new FileOutputStream(file));
			pw.write(sb.toString());
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
//	
//	public static void main(String[] args) {
//		Log l = new Log("login", "2018-12-13 18:05:03", "2018-12-13 18:05:54", "error");
//		System.out.println(l.save("admin"));
//	}

}
