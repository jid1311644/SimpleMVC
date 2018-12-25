package sc.ustc.dao.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import sc.ustc.dao.config.result.ConfigClass;
import sc.ustc.dao.config.result.ConfigClassProperty;
import sc.ustc.dao.config.result.ConfigJDBC;
import sc.ustc.dao.config.result.ConfigResult;

public class Configuration {
	
	public ConfigResult readORMapping(String path) {
		System.out.println("Call Configuration.readORMapping ...");
		//�ֽ�������
		StringBuilder sb = new StringBuilder();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(path)));
			String line = null;
			while((line = br.readLine()) != null) {
				sb.append(line + "\r\n");
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//����jdbc�ڵ�
		System.out.println("Parse JDBC ...");
		String driver = "";
		String url = "";
		String dbUserName = "";
		String dbUserPsw = "";
		ConfigJDBC configJDBC = null;
		String jdbcPoint = sb.toString().split("<jdbc>")[1];
		jdbcPoint = jdbcPoint.substring(0, jdbcPoint.indexOf("</jdbc>"));
		String[] jdbcPropertyPoints = jdbcPoint.split("<property>");
		for(int i = 1; i < jdbcPropertyPoints.length; i++) {
			String propertyPoint = jdbcPropertyPoints[i];
			propertyPoint = propertyPoint.substring(
					0, propertyPoint.indexOf("</property>"));
			String name = propertyPoint.substring(
					propertyPoint.indexOf("<name>") + 6,
					propertyPoint.indexOf("</name>"));
			if(name.equals("driver_class")) {
				driver = propertyPoint.substring(
						propertyPoint.indexOf("<value>") + 7,
						propertyPoint.indexOf("</value>"));
			}
			else if(name.equals("url_path")) {
				String value = propertyPoint.substring(
						propertyPoint.indexOf("<value>") + 7,
						propertyPoint.indexOf("</value>"));
				String timezone = propertyPoint.substring(
						propertyPoint.indexOf("<serverTimezone>") + 16,
						propertyPoint.indexOf("</serverTimezone>"));
				String useSSL = propertyPoint.substring(
						propertyPoint.indexOf("<useSSL>") + 8,
						propertyPoint.indexOf("</useSSL>"));
				url = value + "?serverTimezone=" + timezone
						+ "&useSSL=" + useSSL;
			}
			else if(name.equals("db_username")) {
				dbUserName = propertyPoint.substring(
						propertyPoint.indexOf("<value>") + 7,
						propertyPoint.indexOf("</value>"));
			}
			else if(name.equals("db_userpassword")) {
				dbUserPsw = propertyPoint.substring(
						propertyPoint.indexOf("<value>") + 7,
						propertyPoint.indexOf("</value>"));
			}
		}
		if(driver.equals("") || url.equals("")) {
			//������ڿ�ֵ˵�������ļ���ʽ��������߽���ʧ��
			System.out.println("readOR error:jdbc");
		}
		else {
			configJDBC = new ConfigJDBC(driver, url, dbUserName, dbUserPsw);
		}
		
		//����class�ڵ�
		System.out.println("Parse classes ...");
		LinkedList<ConfigClass> classes = new LinkedList<>();
		String name = "";
		String table = "";
		LinkedList<ConfigClassProperty> classProperties = new LinkedList<>();
		ConfigClass configClass = null;
//		LinkedList<ConfigClassPropert
		String[] classPoints = sb.toString().split("<class>");
		for(int i = 1; i < classPoints.length; i++) {
			String classPoint = classPoints[i].substring(
					0, classPoints[i].indexOf("</class>"));
			String[] classPropertyPoints = classPoint.split("<key-property>");
			name = classPropertyPoints[0].substring(
					classPropertyPoints[0].indexOf("<name>") + 6, 
					classPropertyPoints[0].indexOf("</name>"));
			table = classPropertyPoints[0].substring(
					classPropertyPoints[0].indexOf("<table>") + 7, 
					classPropertyPoints[0].indexOf("</table>"));
			if(name.equals("") || table.equals("")) {
				//�����ļ���ʽ������߽���ʧ��
				System.out.println("readOR error:class name or table");
				break;
			}
			//����class�ڵ���ÿ��key-property�����Խڵ�
			System.out.println("Parse key property ...");
			String nameP = "";
			String column = "";
			String type = "";
			boolean isLazy;
			ConfigClassProperty property = null;
			for(int j = 1; j < classPropertyPoints.length; j++) {
				String classPropertyPoint = classPropertyPoints[j].substring(
						0, classPropertyPoints[j].indexOf("</key-property>"));
				nameP = classPropertyPoint.substring(
						classPropertyPoint.indexOf("<name>") + 6, 
						classPropertyPoint.indexOf("</name>"));
				column = classPropertyPoint.substring(
						classPropertyPoint.indexOf("<column>") + 8, 
						classPropertyPoint.indexOf("</column>"));
				type = classPropertyPoint.substring(
						classPropertyPoint.indexOf("<type>") + 6, 
						classPropertyPoint.indexOf("</type>"));
				String lazy = classPropertyPoint.substring(
						classPropertyPoint.indexOf("<lazy>") + 6, 
						classPropertyPoint.indexOf("</lazy>"));
				isLazy = lazy.equals("true")? true: false;
				if(nameP.equals("") || column.equals("") || 
						type.equals("") || lazy.equals("")) {
					//�����ļ���ʽ������߽���ʧ��
					System.out.println("readOR error:key property " + j);
					break;
				}
				else {
					//�����������������ConfigClassProperty����
					//��װ���ڵ�ǰclass�ڵ��ÿ��property�ڵ�
					property = new ConfigClassProperty(nameP, column, type, isLazy);
					classProperties.add(property);
				}
			}
			if(classProperties.isEmpty()) {
				//�����ļ���ʽ������߽���ʧ��
				System.out.println("readOR error:key property not found!");
				break;
			}
			else {
				configClass = new ConfigClass(name, table);
				while(!classProperties.isEmpty()) {
					configClass.addKeyProperty(classProperties.pollFirst());
				}
			}
			
			//����class�ڵ���ÿ��property�ڵ�
			System.out.println("Parse property ...");
			classPropertyPoints = classPoint.split("<property>");
			nameP = "";
			column = "";
			type = "";
			property = null;
			for(int j = 1; j < classPropertyPoints.length; j++) {
				String classPropertyPoint = classPropertyPoints[j].substring(
						0, classPropertyPoints[j].indexOf("</property>"));
				nameP = classPropertyPoint.substring(
						classPropertyPoint.indexOf("<name>") + 6, 
						classPropertyPoint.indexOf("</name>"));
				column = classPropertyPoint.substring(
						classPropertyPoint.indexOf("<column>") + 8, 
						classPropertyPoint.indexOf("</column>"));
				type = classPropertyPoint.substring(
						classPropertyPoint.indexOf("<type>") + 6, 
						classPropertyPoint.indexOf("</type>"));
				String lazy = classPropertyPoint.substring(
						classPropertyPoint.indexOf("<lazy>") + 6, 
						classPropertyPoint.indexOf("</lazy>"));
				isLazy = lazy.equals("true")? true: false;
				if(nameP.equals("") || column.equals("") || 
						type.equals("") || lazy.equals("")) {
					//�����ļ���ʽ������߽���ʧ��
					System.out.println("readOR error:property" + j);
					break;
				}
				else {
					//�����������������ConfigClassProperty����
					//��װ���ڵ�ǰclass�ڵ��ÿ��property�ڵ�
					property = new ConfigClassProperty(nameP, column, type, isLazy);
					classProperties.add(property);
				}
			}
			//�����������������ConfigClass���󣬷�װ�����class�ڵ������ֵ
			while(!classProperties.isEmpty()) {
				configClass.addProperty(classProperties.pollFirst());
			}
			classes.add(configClass);
		}
		
		if(configJDBC == null) {
			//�����ļ���ʽ������߽���ʧ��
			return null;
		}
		else {
			ConfigResult configResult = new ConfigResult();
			configResult.setJdbc(configJDBC);
			while(!classes.isEmpty()) {
				configResult.addClass(classes.pollFirst());
			}
			System.out.println(configResult.display());
			return configResult;
		}
	}

}
