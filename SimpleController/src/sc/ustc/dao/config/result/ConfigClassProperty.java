package sc.ustc.dao.config.result;

public class ConfigClassProperty {
	
	private String name;
	private String column;
	private String type;
	private boolean isLazy;
	
	public ConfigClassProperty(String name, String column, String type, boolean isLazy) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.column = column;
		this.type = type;
		this.isLazy = isLazy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isLazy() {
		return isLazy;
	}

	public void setLazy(boolean isLazy) {
		this.isLazy = isLazy;
	}
	
	public String display() {
		return "Config Class Property:\r\n"
				+ "name:" + name + "\r\n"
				+ "column:" + column + "\r\n"
				+ "type:" + type + "\r\n"
				+ "lazy:" + isLazy + "\r\n";
	}

}
