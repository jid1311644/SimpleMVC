package sc.ustc.di;

public class DiField {
	
	private String name;
	private String beanRef;
	
	public DiField(String name, String beanRef) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.beanRef = beanRef;
	}

	public String getName() {
		return name;
	}

	public String getBeanRef() {
		return beanRef;
	}

}
