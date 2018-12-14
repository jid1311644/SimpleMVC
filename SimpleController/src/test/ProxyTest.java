package test;

import sc.ustc.beans.ActionBean;

public class ProxyTest {
	
	public String proxyTest(ActionBean bean) {
		System.out.println(bean.getActionName());
		return "ok";
	}

}
