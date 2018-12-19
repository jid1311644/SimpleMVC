package sc.ustc.view.components;

public class TextView extends Component {
	
	private String name;
	private String labelFront;
	private String labelBehind;
	private String aLabel;
	private String value;
	private boolean isClick;
	private String motion;
	
	public TextView(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		this.name = name;
		this.labelFront = "";
		this.labelBehind = "";
		this.isClick = false;
		this.motion = "";
		this.aLabel = "";
		this.value = "";
	}

	public String getName() {
		return name;
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

	public boolean isClick() {
		return isClick;
	}

	public void setClick(boolean isClick) {
		this.isClick = isClick;
	}

	public String getMotion() {
		return motion;
	}

	public void setMotion(String motion) {
		this.motion = motion;
	}
	
	public String getaLabel() {
		return aLabel;
	}

	public void setaLabel(String aLabel) {
		this.aLabel = aLabel;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toJsp() {
		StringBuilder jsp = new StringBuilder();
		jsp.append("<div>" + labelFront + value + "\r\n");
		if(isClick) {
			//<a href="/UseSC/views/login.jsp">Logout</a>
			jsp.append("<a href=\"" + motion + "\">");
			jsp.append(aLabel + "</a>\r\n");
		}
		jsp.append(labelBehind + "</div>\r\n");
		return jsp.toString();
	}

}
