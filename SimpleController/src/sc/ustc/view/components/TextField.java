package sc.ustc.view.components;

public class TextField extends Component {
	
	private String name;
	private String labelFront;
	private String labelBehind;
	private boolean isPassword;
	
	public TextField(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		this.name = name;
		this.isPassword = false;
		this.labelFront = "";
		this.labelBehind = "";
	}

	public String[] getLabel() {
		String[] label = new String[2];
		label[0] = labelFront;
		label[1] = labelBehind;
		return label;
	}

	public void setLabelFront(String labelFront) {
		this.labelFront = labelFront;
	}
	
	public void setLabelBehind(String labelBehind) {
		this.labelBehind = labelBehind;
	}

	public boolean isPassword() {
		return isPassword;
	}

	public void setPassword(boolean isPassword) {
		this.isPassword = isPassword;
	}

	public String getName() {
		return name;
	}
	
	public String toJsp() {
		StringBuilder jsp = new StringBuilder();
		//<div>Password:<input type="password" name="password"/></div>
		jsp.append("<div>" + labelFront + "\r\n");
		jsp.append("<input type=\"");
		if(isPassword) {
			jsp.append("password");
		}
		else {
			jsp.append("text");
		}
		jsp.append("\" name=\"" + name + "\"/>\r\n");
		jsp.append(labelBehind + "</div>\r\n");
		return jsp.toString();
	}

}
