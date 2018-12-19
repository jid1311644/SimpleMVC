package sc.ustc.view.components;

public class ListOption {
	
	private String value;
	private String item;
	private boolean isSelected;
	
	public ListOption(String value) {
		// TODO Auto-generated constructor stub
		this.value = value;
		this.item = "";
		this.isSelected = false;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	

}
