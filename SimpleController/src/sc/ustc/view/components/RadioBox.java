package sc.ustc.view.components;

public class RadioBox {
	
	private String labelFront;
	private String labelBehind;
	private String value;
	private boolean isChecked;

	public RadioBox(String value) {
		// TODO Auto-generated constructor stub
		this.value = value;
		this.labelBehind = "";
		this.labelFront = "";
		this.isChecked = false;
	}

	public String[] getLabel() {
		String[] label = new String[2];
		label[0] = labelFront;
		label[1] = labelBehind;
		return label;
	}

	public void setLabel(String labelFront, String labelBehind) {
		this.labelFront = labelFront;
		this.labelBehind = labelBehind;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	
	
}
