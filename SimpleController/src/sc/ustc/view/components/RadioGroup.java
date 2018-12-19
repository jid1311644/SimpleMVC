package sc.ustc.view.components;

import java.util.LinkedList;

public class RadioGroup extends Component {
	
	private String name;
	private String labelFront;
	private String labelBehind;
	private LinkedList<RadioBox> radioBoxs;
	
	public RadioGroup(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		this.name = name;
		this.labelFront = "";
		this.labelBehind = "";
		this.radioBoxs = new LinkedList<>();
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

	public LinkedList<RadioBox> getRadioBoxs() {
		return radioBoxs;
	}

	public void addRadioBox(RadioBox radioBox) {
		this.radioBoxs.add(radioBox);
	}
	
	public String toJsp() {
		StringBuilder jsp = new StringBuilder();
		jsp.append("<div>" + labelFront + "\r\n");
		while(!radioBoxs.isEmpty()) {
			RadioBox rb = radioBoxs.pollFirst();
			jsp.append(rb.getLabel()[0] + "<input name=\"" + name + "\" "
					+ "type=\"radio\" value=\"" + rb.getValue() + "\"");
			if(rb.isChecked()) {
				jsp.append(" checked=\"checked\"");
			}
			jsp.append(">" + rb.getLabel()[1] + "\r\n");
		}
		jsp.append(labelBehind + "</div>\r\n");
		return jsp.toString();
	}
	
}
