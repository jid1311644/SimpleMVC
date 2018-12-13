package sc.ustc.beans;

public class InterceptorBean {

	private String interceptorName;
	private String interceptorClass;
	private String interceptorPredo;
	private String interceptorAfterdo;

	public InterceptorBean(String interceptorName, String interceptorClass, 
			String interceptorPredo, String interceptorAfterdo) {
		// TODO Auto-generated constructor stub
		this.interceptorName = interceptorName;
		this.interceptorClass = interceptorClass;
		this.interceptorPredo = interceptorPredo;
		this.interceptorAfterdo = interceptorAfterdo;
	}

	public String getInterceptorName() {
		return interceptorName;
	}

	public String getInterceptorClass() {
		return interceptorClass;
	}

	public String getInterceptorPredo() {
		return interceptorPredo;
	}

	public String getInterceptorAfterdo() {
		return interceptorAfterdo;
	}
	
	public void display() {
		System.out.println("interceptor-name:" + interceptorName);
		System.out.println("interceptor-class:" + interceptorClass);
		System.out.println("interceptor-predo:" + interceptorPredo);
		System.out.println("interceptor-after:" + interceptorAfterdo);
	}
	
}
