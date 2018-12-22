package sc.ustc.dao.config.result;

public class ConfigJDBC {
	
	private String driver;
	private String url;
	private String dbUserName;
	private String dbUserPassword;
	
	public ConfigJDBC(String driver, String url, String name, String psw) {
		// TODO Auto-generated constructor stub
		this.driver = driver;
		this.url = url;
		this.dbUserName = name;
		this.dbUserPassword = psw;
	}
	
	public String getDriver() {
		return driver;
	}
	public String getUrl() {
		return url;
	}
	public String getDbUserName() {
		return dbUserName;
	}
	public String getDbUserPassword() {
		return dbUserPassword;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}

	public void setDbUserPassword(String dbUserPassword) {
		this.dbUserPassword = dbUserPassword;
	}
	
	public String display() {
		return "JDBC:\r\ndriver:" + driver + "\r\n" + 
				"url:" + url + "\r\n" + 
				"username:" + dbUserName + "\r\n" + 
				"password:" + dbUserPassword + "\r\n";
	}

}
