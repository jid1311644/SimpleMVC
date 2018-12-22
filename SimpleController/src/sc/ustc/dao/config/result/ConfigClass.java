package sc.ustc.dao.config.result;

import java.util.LinkedList;

public class ConfigClass {
	
	private String name;
	private String table;
	private LinkedList<ConfigClassProperty> keyProperties;
	private LinkedList<ConfigClassProperty> properties;
	
	public ConfigClass(String name, String table) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.table = table;
		keyProperties = new LinkedList<>();
		properties = new LinkedList<>();
	}

	public String getName() {
		return name;
	}

	public String getTable() {
		return table;
	}
	
	public LinkedList<ConfigClassProperty> getKeyProperties() {
		return keyProperties;
	}

	public LinkedList<ConfigClassProperty> getProperties() {
		return properties;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTable(String table) {
		this.table = table;
	}
	
	public void addKeyProperty(ConfigClassProperty keyProperty) {
		this.keyProperties.add(keyProperty);
	}

	public void addProperty(ConfigClassProperty property) {
		this.properties.add(property);
	}
	
	public String display() {
		String ps0 = "";
		String ps1 = "";
		for(ConfigClassProperty p: keyProperties) {
			ps0 += p.display();
		}
		for(ConfigClassProperty p: properties) {
			ps1 += p.display();
		}
		return "Config Class:\r\n"
				+ "name:" + name + "\r\n"
				+ "table:" + table + "\r\n"
				+ "key:\r\n" + ps0
				+ "property:\r\n" + ps1;
	}

}
