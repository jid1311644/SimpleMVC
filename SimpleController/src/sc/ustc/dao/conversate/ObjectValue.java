package sc.ustc.dao.conversate;

public class ObjectValue {
	//��װ��������Ҫ���в��������Լ���ֵ
	private String propertyName;
	private String propertyValue;
	private String propertyType;
	private boolean isLazy;
	
	public ObjectValue(String propertyName, String propertyValue, 
			String propertyType, boolean isLazy) {
		// TODO Auto-generated constructor stub
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
		this.propertyType = propertyType;
		this.isLazy = isLazy;
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	
	public String getPropertyValue() {
		return propertyValue;
	}
	
	public String getPropertyType() {
		return propertyType;
	}

	public boolean isLazy() {
		return isLazy;
	}
		
}
