package sc.ustc.view.components;

public class CheckBox extends Component {
	
	private String name;
	private String labelFront;
	private String labelBehind;
	private boolean isChecked;
	
	public CheckBox(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		this.name = name;
		this.labelFront = "";
		this.labelBehind = "";
		this.isChecked = false;
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

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getName() {
		return name;
	}
	
	public String toJsp() {
		StringBuilder jsp = new StringBuilder();
		//<div>qwe<input type="checkbox" name="isAccept" checked="true"></div>
		jsp.append("<div>" + labelFront + "\r\n");
		jsp.append("<input type=\"checkbox\" name=\"" + 
					name + "\" ");
		if(isChecked) {
			jsp.append("checked=\"checked\"");
		}
		jsp.append(">\r\n");
		jsp.append(labelBehind + "</div>\r\n");
		return jsp.toString();
	}

}
