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
		//����or_mapping.xml�ļ�
		this.configResult = new Configuration().readORMapping(path);
	}
	
	public Object selectObject(Object property, Object value) {	
		System.out.println("Call Conversation.selectObject ...");
		//Ѱ�Ҷ�Ӧ��class
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
				//������
				for(ConfigClassProperty keyCcp: orClass.getKeyProperties()) {
					Field field = c.getDeclaredField(keyCcp.getName());
					field.setAccessible(true);
					//��ȡҪselect����������
					String fieldProperty = (String) field.get(property);
					if(fieldProperty != null) {
						properties.add(new ObjectValue(keyCcp.getColumn(), 
								null, null, keyCcp.isLazy()));
					}
					//��ȡselect where�е�������ֵ
					String fieldValue = (String) field.get(value);
					if(fieldValue != null) {
						values.add(new ObjectValue(keyCcp.getColumn(), 
								fieldValue, keyCcp.getType(), keyCcp.isLazy()));
					}
					
				}
				//��ͨ����
				for(ConfigClassProperty ccp: orClass.getProperties()) {
					Field field = c.getDeclaredField(ccp.getName());
					field.setAccessible(true);
					//��ȡҪselect������
					String fieldProperty = (String) field.get(property);
					if(fieldProperty != null) {
						properties.add(new ObjectValue(ccp.getColumn(), 
								null, null, ccp.isLazy()));
					}
					//��ȡselect where�е�������ֵ
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
			
			//�ж����Ե�lazy �Ƿ��ӳ�ִ��
			if(properties.size() == 1 && properties.getFirst().isLazy()) {
				return null;
			}
			if(values.size() == 1 && values.getFirst().isLazy()) {
				return null;
			}
			//����sql���
			StringBuilder sql = new StringBuilder();
			sql.append("select ");
			for(ObjectValue ovP: properties) {
				//��ȡҪselect������
				if(!ovP.isLazy()) {
					sql.append(ovP.getPropertyName() + ",");
				}
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" from " + orClass.getTable() + " where ");
			for(ObjectValue ovV: values) {
				//��ȡҪselect������
				if(!ovV.isLazy()) {
					sql.append(ovV.getPropertyName() + "=? and ");
				}
			}
			sql.delete(sql.length() - 5, sql.length() - 1);
			System.out.println("SQL:" + sql.toString());
			
			//�������ݿ� ִ��sql���
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
					//ִ��sql���
					PreparedStatement ps = connection.prepareStatement(
							sql.toString());
					int i = 1;
					for(ObjectValue ovV: values) {
						//��ȡsql���where�е�ֵ
						//���ݲ�ͬ���������͸���������ֵ
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
		//Ѱ�Ҷ�Ӧ��class
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
				//��������
				for(ConfigClassProperty keyCcp: orClass.getKeyProperties()) {
					Field field = c.getDeclaredField(keyCcp.getName());
					field.setAccessible(true);
					//��ȡ������������ֵ
					String fieldValue = (String) field.get(value);
					if(fieldValue != null) {
						values.add(new ObjectValue(keyCcp.getColumn(), 
								fieldValue, keyCcp.getType(), keyCcp.isLazy()));
					}
					else {
						//��������Ե�ֵΪ�գ����ܲ��룬����false
						return false;
					}
				}
				for(ConfigClassProperty ccp: orClass.getProperties()) {
					Field field = c.getDeclaredField(ccp.getName());
					field.setAccessible(true);
					//��ȡ������������ֵ
					String fieldValue = (String) field.get(value);
					if(fieldValue != null) {
						values.add(new ObjectValue(ccp.getColumn(), 
								fieldValue, ccp.getType(), ccp.isLazy()));
					}
					else {
						//��������Ե�ֵΪ�գ����ܲ��룬����false
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
			
			//����sql���
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
			
			//�������ݿ� ִ��sql���
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
					//ִ��sql���
					PreparedStatement ps = connection.prepareStatement(
							sql.toString());
					int i = 1;
					for(ObjectValue ovV: values) {
						//���ݲ�ͬ���������͸���������ֵ
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
		//Ѱ�Ҷ�Ӧ��class
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
				//������
				for(ConfigClassProperty keyCcp: orClass.getKeyProperties()) {
					Field field = c.getDeclaredField(keyCcp.getName());
					field.setAccessible(true);
					//��ȡҪupdate��������������ֵ
					String fieldProperty = (String) field.get(property);
					if(fieldProperty != null) {
						if(keyCcp.getType().equals("Boolean")) {
							fieldProperty = fieldProperty.equals("true")? "1": "0";
						}
						properties.add(new ObjectValue(keyCcp.getColumn(), 
								fieldProperty, keyCcp.getType(), keyCcp.isLazy()));
					}
					//��ȡupdate where�е�������ֵ
					String fieldValue = (String) field.get(value);
					if(fieldValue != null) {
						if(keyCcp.getType().equals("Boolean")) {
							fieldProperty = fieldProperty.equals("true")? "1": "0";
						}
						values.add(new ObjectValue(keyCcp.getColumn(), 
								fieldValue, keyCcp.getType(), keyCcp.isLazy()));
					}
				}
				//��ͨ����
				for(ConfigClassProperty ccp: orClass.getProperties()) {
					Field field = c.getDeclaredField(ccp.getName());
					field.setAccessible(true);
					//��ȡҪupdate��������������ֵ
					String fieldProperty = (String) field.get(property);
					if(fieldProperty != null) {
						if(ccp.getType().equals("Boolean")) {
							fieldProperty = fieldProperty.equals("true")? "1": "0";
						}
						properties.add(new ObjectValue(ccp.getColumn(), 
								null, null, ccp.isLazy()));
					}
					//��ȡupdate where�е�������ֵ
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
			
			//����sql���
			StringBuilder sql = new StringBuilder();
			sql.append("update " + orClass.getTable() + " set ");
			for(ObjectValue ovP: properties) {
				//��ȡҪupdate�����Ժ���ֵ
				sql.append(ovP.getPropertyName() + "='" + 
						ovP.getPropertyValue() + "',");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where ");
			for(ObjectValue ovV: values) {
				//��ȡupdate where���������Ժ�ֵ
				sql.append(ovV.getPropertyName() + "='" + 
						ovV.getPropertyValue() + "' and ");
			}
			sql.delete(sql.length() - 5, sql.length() - 1);
			System.out.println("SQL:" + sql.toString());
			
			//�������ݿ� ִ��sql���
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
					//ִ��sql���
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
		//Ѱ�Ҷ�Ӧ��class
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
					//��ȡwhere��������������ֵ
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
					//��ȡwhere��������������ֵ
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
			
			//����sql���
			StringBuilder sql = new StringBuilder();
			sql.append("delete from " + orClass.getTable() + " where ");
			for(ObjectValue ovV: values) {
				//��ȡupdate where���������Ժ�ֵ
				sql.append(ovV.getPropertyName() + "='" + 
							ovV.getPropertyValue() + "' and ");
			}
			sql.delete(sql.length() - 5, sql.length() - 1);
			System.out.println("SQL:" + sql.toString());
			
			//�������ݿ� ִ��sql���
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
					//ִ��sql���
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
