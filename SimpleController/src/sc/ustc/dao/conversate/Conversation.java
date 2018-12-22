package sc.ustc.dao.conversate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import sc.ustc.dao.config.Configuration;
import sc.ustc.dao.config.result.ConfigClass;
import sc.ustc.dao.config.result.ConfigClassProperty;
import sc.ustc.dao.config.result.ConfigJDBC;
import sc.ustc.dao.config.result.ConfigResult;

public class Conversation {
	
	private static String path = Thread.currentThread().
			getContextClassLoader().getResource("or_mapping.xml").
			getPath();
	private ConfigResult configResult;
	
	public Conversation() {
		// TODO Auto-generated constructor stub
		//解析or_mapping.xml文件
		this.configResult = new Configuration().readORMapping(path);
	}
	
	public Object selectObject(Object property, Object value) {	
		System.out.println("Call Conversation.selectObject ...");
		//寻找对应的class
		Class<?> c = property.getClass();
		ConfigClass orClass = null;
		for(ConfigClass cc: configResult.getClasses()) {
			if(cc.getName().equals(c.getName())) {
				orClass = cc;
				break;
			}
		}
		if(orClass != null) {
			LinkedList<ObjectValue> properties = new LinkedList<>();
			LinkedList<ObjectValue> values = new LinkedList<>();
			try {
				//主属性
				for(ConfigClassProperty keyCcp: orClass.getKeyProperties()) {
					Field field = c.getDeclaredField(keyCcp.getName());
					field.setAccessible(true);
					//提取要select的属性列名
					String fieldProperty = (String) field.get(property);
					if(fieldProperty != null) {
						properties.add(new ObjectValue(keyCcp.getColumn(), 
								null, null, keyCcp.isLazy()));
					}
					//提取select where中的属性与值
					String fieldValue = (String) field.get(value);
					if(fieldValue != null) {
						values.add(new ObjectValue(keyCcp.getColumn(), 
								fieldValue, keyCcp.getType(), keyCcp.isLazy()));
					}
					
				}
				//普通属性
				for(ConfigClassProperty ccp: orClass.getProperties()) {
					Field field = c.getDeclaredField(ccp.getName());
					field.setAccessible(true);
					//提取要select的属性
					String fieldProperty = (String) field.get(property);
					if(fieldProperty != null) {
						properties.add(new ObjectValue(ccp.getColumn(), 
								null, null, ccp.isLazy()));
					}
					//提取select where中的属性与值
					String fieldValue = (String) field.get(value);
					if(fieldValue != null) {
						values.add(new ObjectValue(ccp.getColumn(), 
								fieldValue, ccp.getType(), ccp.isLazy()));
					}
				}
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//判断属性的lazy 是否延迟执行
			if(properties.size() == 1 && properties.getFirst().isLazy()) {
				return null;
			}
			if(values.size() == 1 && values.getFirst().isLazy()) {
				return null;
			}
			//生成sql语句
			StringBuilder sql = new StringBuilder();
			sql.append("select ");
			for(ObjectValue ovP: properties) {
				//提取要select的属性
				if(!ovP.isLazy()) {
					sql.append(ovP.getPropertyName() + ",");
				}
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" from " + orClass.getTable() + " where ");
			for(ObjectValue ovV: values) {
				//提取要select的属性
				if(!ovV.isLazy()) {
					sql.append(ovV.getPropertyName() + "=? and ");
				}
			}
			sql.delete(sql.length() - 5, sql.length() - 1);
			System.out.println("SQL:" + sql.toString());
			
			//连接数据库 执行sql语句
			ConfigJDBC configJDBC = configResult.getJdbc();
			if(configJDBC != null) {
				try {
					Class.forName(configJDBC.getDriver());
					System.out.println("Success loading Driver!");
					System.out.println("Connecting ...");
					Connection connection = DriverManager.getConnection(
							configJDBC.getUrl(), configJDBC.getDbUserName(), 
							configJDBC.getDbUserPassword());
					System.out.println("Success connecting DBMS!");
					//执行sql语句
					PreparedStatement ps = connection.prepareStatement(
							sql.toString());
					int i = 1;
					for(ObjectValue ovV: values) {
						//提取sql语句where中的值
						//根据不同的属性类型给“？”赋值
						switch (ovV.getPropertyType()) {
						case "String":
							ps.setString(i++, ovV.getPropertyValue());
							break;
						case "Integer":
							ps.setInt(i++, Integer.parseInt(ovV.getPropertyValue()));
							break;
						case "Double":
							ps.setDouble(i++, Double.parseDouble(ovV.getPropertyValue()));
							break;
						case "Boolean":
							ps.setBoolean(i++, Boolean.parseBoolean(ovV.getPropertyValue()));
							break;
						default:
							break;
						}
					}
					return ps.executeQuery();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					System.out.println("Error loading DBMS Driver!");
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("Error connecting or SQL!");
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public boolean insertObject(Object value) {
		System.out.println("Call Conversation.insertObject ...");
		//寻找对应的class
		Class<?> c = value.getClass();
		ConfigClass orClass = null;
		for(ConfigClass cc: configResult.getClasses()) {
			if(cc.getName().equals(c.getName())) {
				orClass = cc;
				break;
			}
		}
		if(orClass != null) {
			LinkedList<ObjectValue> values = new LinkedList<>();
			try {
				//所有属性
				for(ConfigClassProperty keyCcp: orClass.getKeyProperties()) {
					Field field = c.getDeclaredField(keyCcp.getName());
					field.setAccessible(true);
					//提取所有属性与其值
					String fieldValue = (String) field.get(value);
					if(fieldValue != null) {
						values.add(new ObjectValue(keyCcp.getColumn(), 
								fieldValue, keyCcp.getType(), keyCcp.isLazy()));
					}
					else {
						//如果有属性的值为空，则不能插入，返回false
						return false;
					}
				}
				for(ConfigClassProperty ccp: orClass.getProperties()) {
					Field field = c.getDeclaredField(ccp.getName());
					field.setAccessible(true);
					//提取所有属性与其值
					String fieldValue = (String) field.get(value);
					if(fieldValue != null) {
						values.add(new ObjectValue(ccp.getColumn(), 
								fieldValue, ccp.getType(), ccp.isLazy()));
					}
					else {
						//如果有属性的值为空，则不能插入，返回false
						return false;
					}
				}
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//生成sql语句
			StringBuilder sql = new StringBuilder();
			sql.append("insert into " + orClass.getTable() + " (");
			int size = 0;
			for(ObjectValue ovV: values) {
				sql.append(ovV.getPropertyName() + ",");
				size++;
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(") values (");
			for(int i = 0; i < size; i++) {
				sql.append("?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(")");
			System.out.println("SQL:" + sql.toString());
			
			//连接数据库 执行sql语句
			ConfigJDBC configJDBC = configResult.getJdbc();
			if(configJDBC != null) {
				try {
					Class.forName(configJDBC.getDriver());
					System.out.println("Success loading Driver!");
					System.out.println("Connecting ...");
					Connection connection = DriverManager.getConnection(
							configJDBC.getUrl(), configJDBC.getDbUserName(), 
							configJDBC.getDbUserPassword());
					System.out.println("Success connecting DBMS!");
					//执行sql语句
					PreparedStatement ps = connection.prepareStatement(
							sql.toString());
					int i = 1;
					for(ObjectValue ovV: values) {
						//根据不同的属性类型给“？”赋值
						System.out.println(ovV.getPropertyName() + " " + ovV.getPropertyType() + " " + ovV.getPropertyValue());
						switch (ovV.getPropertyType()) {
						case "String":
							ps.setString(i++, ovV.getPropertyValue());
							break;
						case "Integer":
							ps.setInt(i++, Integer.parseInt(ovV.getPropertyValue()));
							break;
						case "Double":
							ps.setDouble(i++, Double.parseDouble(ovV.getPropertyValue()));
							break;
						case "Boolean":
							ps.setBoolean(i++, Boolean.parseBoolean(ovV.getPropertyValue()));
							break;
						default:
							break;
						}
					}
					ps.executeUpdate();
					return true;
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					System.out.println("Error loading DBMS Driver!");
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("Error connecting or SQL!");
					
				}
			}
		}
		return false;
	}
	
	public boolean updateObject(Object property, Object value) {
		System.out.println("Call Conversation.updateObject ...");
		//寻找对应的class
		Class<?> c = property.getClass();
		ConfigClass orClass = null;
		for(ConfigClass cc: configResult.getClasses()) {
			if(cc.getName().equals(c.getName())) {
				orClass = cc;
				break;
			}
		}
		if(orClass != null) {
			LinkedList<ObjectValue> properties = new LinkedList<>();
			LinkedList<ObjectValue> values = new LinkedList<>();
			try {
				//主属性
				for(ConfigClassProperty keyCcp: orClass.getKeyProperties()) {
					Field field = c.getDeclaredField(keyCcp.getName());
					field.setAccessible(true);
					//提取要update的属性列名和新值
					String fieldProperty = (String) field.get(property);
					if(fieldProperty != null) {
						if(keyCcp.getType().equals("Boolean")) {
							fieldProperty = fieldProperty.equals("true")? "1": "0";
						}
						properties.add(new ObjectValue(keyCcp.getColumn(), 
								fieldProperty, keyCcp.getType(), keyCcp.isLazy()));
					}
					//提取update where中的属性与值
					String fieldValue = (String) field.get(value);
					if(fieldValue != null) {
						if(keyCcp.getType().equals("Boolean")) {
							fieldProperty = fieldProperty.equals("true")? "1": "0";
						}
						values.add(new ObjectValue(keyCcp.getColumn(), 
								fieldValue, keyCcp.getType(), keyCcp.isLazy()));
					}
				}
				//普通属性
				for(ConfigClassProperty ccp: orClass.getProperties()) {
					Field field = c.getDeclaredField(ccp.getName());
					field.setAccessible(true);
					//提取要update的属性列名和新值
					String fieldProperty = (String) field.get(property);
					if(fieldProperty != null) {
						if(ccp.getType().equals("Boolean")) {
							fieldProperty = fieldProperty.equals("true")? "1": "0";
						}
						properties.add(new ObjectValue(ccp.getColumn(), 
								null, null, ccp.isLazy()));
					}
					//提取update where中的属性与值
					String fieldValue = (String) field.get(value);
					if(fieldValue != null) {
						if(ccp.getType().equals("Boolean")) {
							fieldProperty = fieldProperty.equals("true")? "1": "0";
						}
						values.add(new ObjectValue(ccp.getColumn(), 
								fieldValue, ccp.getType(), ccp.isLazy()));
					}
				}
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//生成sql语句
			StringBuilder sql = new StringBuilder();
			sql.append("update " + orClass.getTable() + " set ");
			for(ObjectValue ovP: properties) {
				//提取要update的属性和新值
				sql.append(ovP.getPropertyName() + "='" + 
						ovP.getPropertyValue() + "',");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where ");
			for(ObjectValue ovV: values) {
				//提取update where条件的属性和值
				sql.append(ovV.getPropertyName() + "='" + 
						ovV.getPropertyValue() + "' and ");
			}
			sql.delete(sql.length() - 5, sql.length() - 1);
			System.out.println("SQL:" + sql.toString());
			
			//连接数据库 执行sql语句
			ConfigJDBC configJDBC = configResult.getJdbc();
			if(configJDBC != null) {
				try {
					Class.forName(configJDBC.getDriver());
					System.out.println("Success loading Driver!");
					System.out.println("Connecting ...");
					Connection connection = DriverManager.getConnection(
							configJDBC.getUrl(), configJDBC.getDbUserName(), 
							configJDBC.getDbUserPassword());
					System.out.println("Success connecting DBMS!");
					//执行sql语句
					PreparedStatement ps = connection.prepareStatement(
							sql.toString());
					ps.executeUpdate();
					return true;
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					System.out.println("Error loading DBMS Driver!");
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("Error connecting or SQL!");
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	public boolean deleteObject(Object value) {
		System.out.println("Call Conversation.deleteObject ...");
		//寻找对应的class
		Class<?> c = value.getClass();
		ConfigClass orClass = null;
		for(ConfigClass cc: configResult.getClasses()) {
			if(cc.getName().equals(c.getName())) {
				orClass = cc;
				break;
			}
		}
		if(orClass != null) {
			LinkedList<ObjectValue> values = new LinkedList<>();
			try {
				for(ConfigClassProperty keyCcp: orClass.getKeyProperties()) {
					Field field = c.getDeclaredField(keyCcp.getName());
					field.setAccessible(true);
					//提取where条件中属性与其值
					String fieldValue = (String) field.get(value);
					if(fieldValue != null) {
						if(keyCcp.getType().equals("Boolean")) {
							fieldValue = fieldValue.equals("true")? "1":"0";
						}
						values.add(new ObjectValue(keyCcp.getColumn(), 
								fieldValue, keyCcp.getType(), keyCcp.isLazy()));
					}
				}
				for(ConfigClassProperty ccp: orClass.getProperties()) {
					Field field = c.getDeclaredField(ccp.getName());
					field.setAccessible(true);
					//提取where条件中属性与其值
					String fieldValue = (String) field.get(value);
					if(fieldValue != null) {
						if(ccp.getType().equals("Boolean")) {
							fieldValue = fieldValue.equals("true")? "1":"0";
						}
						values.add(new ObjectValue(ccp.getColumn(), 
								fieldValue, ccp.getType(), ccp.isLazy()));
					}
				}
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//生成sql语句
			StringBuilder sql = new StringBuilder();
			sql.append("delete from " + orClass.getTable() + " where ");
			for(ObjectValue ovV: values) {
				//提取update where条件的属性和值
				sql.append(ovV.getPropertyName() + "='" + 
							ovV.getPropertyValue() + "' and ");
			}
			sql.delete(sql.length() - 5, sql.length() - 1);
			System.out.println("SQL:" + sql.toString());
			
			//连接数据库 执行sql语句
			ConfigJDBC configJDBC = configResult.getJdbc();
			if(configJDBC != null) {
				try {
					Class.forName(configJDBC.getDriver());
					System.out.println("Success loading Driver!");
					System.out.println("Connecting ...");
					Connection connection = DriverManager.getConnection(
							configJDBC.getUrl(), configJDBC.getDbUserName(), 
							configJDBC.getDbUserPassword());
					System.out.println("Success connecting DBMS!");
					//执行sql语句
					PreparedStatement ps = connection.prepareStatement(
							sql.toString());
					ps.executeUpdate();
					return true;
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					System.out.println("Error loading DBMS Driver!");
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("Error connecting or SQL!");
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public ConfigResult getConfigResult() {
		return configResult;
	}

}
