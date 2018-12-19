package sc.ustc.view.components;

import java.util.LinkedList;

public class ListView extends Component {
	
	private String name;
	private String labelFront;
	private String labelBehind;
	private LinkedList<ListOption> options;
	
	public ListView(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		this.name = name;
		this.labelFront = "";
		this.labelBehind = "";
		this.options = new LinkedList<>();
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

	public LinkedList<ListOption> getOptions() {
		return options;
	}

	public void addOption(ListOption option) {
		this.options.add(option);
	}
	
	public String toJsp() {
		StringBuilder jsp = new StringBuilder();
		jsp.append("<div>" + labelFront + "\r\n");
		jsp.append("<select name=\"" + name + "\">\r\n");
		while(!options.isEmpty()) {
			ListOption lo = options.pollFirst();
			jsp.append("<option value=\"" + lo.getValue() + "\"");
			if(lo.isSelected()) {
				jsp.append(" selected=\"selected\"");
			}
			jsp.append(">" + lo.getItem() + "</option>\r\n");
		}
		jsp.append("</select>\r\n" + labelBehind + "</div>\r\n");
		return jsp.toString();
	}

}
