package sc.ustc.view.components;

public class ButtonView extends Component {
	
	private String name;
	private String labelFront;
	private String labelBehind;
	private String value;
	
	public ButtonView(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		this.name = name;
		this.labelFront = "";
		this.labelBehind = "";
		this.value = "";
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}
	
	public String toJsp() {
		StringBuilder jsp = new StringBuilder();
		//<div><input type="submit" name="login" value="Login"/></div>
		jsp.append("<div>" + labelFront + "\r\n");
		jsp.append("<input type=\"submit\" name=\"" + 
					name + "\" value=\"" + value + 
					"\"/>\r\n");
		jsp.append(labelBehind + "</div>\r\n");
		return jsp.toString();
	}

}
