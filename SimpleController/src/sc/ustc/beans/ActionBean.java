package sc.ustc.beans;

import java.util.HashMap;
import java.util.LinkedList;

public class ActionBean {
	
	private String actionName;
	private String className;
	private String methodName;
	private ActionResult resultOK;
	private ActionResult resultError;
	private LinkedList<ActionInterceptorRef> actionInterceptorRefs;
	private LinkedList<InterceptorBean> actionInterceptors;
	
	public ActionBean(String actionName, String className, String methodName) {
		// TODO Auto-generated constructor stub
		this.actionName = actionName;
		this.className = className;
		this.methodName = methodName;
		actionInterceptorRefs = new LinkedList<>();
	}
	
	public void setResultOK(String resultName, String resultType, String resultValue) {
		this.resultOK = new ActionResult(resultName, resultType, resultValue);
	}
	
	public void setResultError(String resultName, String resultType, String resultValue) {
		this.resultError = new ActionResult(resultName, resultType, resultValue);
	}
	
	public void addActionInterceptorRefs(String interceptroRefName) {
		this.actionInterceptorRefs.add(new ActionInterceptorRef(interceptroRefName));
	}

	public void setActionInterceptors(LinkedList<InterceptorBean> actionInterceptors) {
		this.actionInterceptors = actionInterceptors;
	}

	public String getActionName() {
		return actionName;
	}

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getResultName(String result) {
		if(result.equals("ok")) {
			return resultOK.getResultName();
		}
		else if(result.equals("error")){
			return resultError.getResultName();
		}
		else {
			return null;
		}
	}
	
	public String getResultType(String result) {
		if(result.equals("ok")) {
			return resultOK.getResultType();
		}
		else if(result.equals("error")){
			return resultError.getResultType();
		}
		else {
			return null;
		}
	}
	
	public String getResultValue(String result) {
		if(result.equals("ok")) {
			return resultOK.getResultValue();
		}
		else if(result.equals("error")){
			return resultError.getResultValue();
		}
		else {
			return null;
		}
	}
	
	public LinkedList<String> getActionInterceptorsName() {
		LinkedList<String> names = new LinkedList<>();
		for(ActionInterceptorRef i:this.actionInterceptorRefs) {
			names.add(i.getInterceptroRefName());
		}
		return names;
	}

	public LinkedList<InterceptorBean> getActionInterceptors() {
		return actionInterceptors;
	}

	public void display() {
		System.out.println("ActionBean:\r\naction-name:" + actionName + "\r\n"
				+ "class-name:" + className + "\r\n"
				+ "method-name:" + methodName + "\r\n"
				+ "result-OK-name:" + resultOK.getResultName() + "\r\n"
				+ "result-OK-type:" + resultOK.getResultType() + "\r\n"
				+ "result-OK-value:" + resultOK.getResultValue() + "\r\n"
				+ "result-Error-name:" + resultError.getResultName() + "\r\n"
				+ "result-Error-type:" + resultError.getResultType() + "\r\n"
				+ "result-Error-value:" + resultError.getResultValue());
		int count = 0;
		for(ActionInterceptorRef i:this.actionInterceptorRefs) {
			System.out.println("interceptors-" + (count++) + ":" + i.getInterceptroRefName());
		}
		System.out.println();
	}
	
}


class ActionResult {
	private String resultName;
	private String resultType;
	private String resultValue;
	
	public ActionResult(String resultName, String resultType, String resultValue) {
		// TODO Auto-generated constructor stub
		this.resultName = resultName;
		this.resultType = resultType;
		this.resultValue = resultValue;
	}

	public String getResultName() {
		return resultName;
	}

	public String getResultType() {
		return resultType;
	}

	public String getResultValue() {
		return resultValue;
	}
	
}


class ActionInterceptorRef {
	private String interceptroRefName;
	
	public ActionInterceptorRef(String interceptroRefName) {
		// TODO Auto-generated constructor stub
		this.interceptroRefName = interceptroRefName;
	}

	public String getInterceptroRefName() {
		return interceptroRefName;
	}
	
}



