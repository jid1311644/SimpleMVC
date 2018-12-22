package sc.ustc.dao.config.result;

import java.util.LinkedList;

public class ConfigResult {
	
	private ConfigJDBC jdbc;
	private LinkedList<ConfigClass> classes;
	
	public ConfigResult() {
		// TODO Auto-generated constructor stub
		this.classes = new LinkedList<>();
	}
	
	public ConfigJDBC getJdbc() {
		return jdbc;
	}

	public LinkedList<ConfigClass> getClasses() {
		return classes;
	}

	public void setJdbc(ConfigJDBC jdbc) {
		this.jdbc = jdbc;
	}

	public void addClass(ConfigClass clasS) {
		this.classes.add(clasS);
	}
	
	public String display() {
		String cs = "";
		for(ConfigClass c: classes) {
			cs += "\r\n" + c.display();
		}
		return jdbc.display() + cs;
	}

}
