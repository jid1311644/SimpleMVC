package sc.ustc.tools;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import sc.ustc.beans.ActionBean;
import sc.ustc.beans.InterceptorBean;

public class CglibProxy implements MethodInterceptor {
	
	private ActionBean actionBean;
	public CglibProxy(ActionBean bean) {
		// TODO Auto-generated constructor stub
		this.actionBean = bean;
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		// TODO Auto-generated method stub
		//执行action之前执行interceptor
		interceptor(actionBean.getActionInterceptors(), true, 
				(HttpServletRequest)args[0], actionBean.getActionName(), null);
		//执行action
		String result = (String)proxy.invokeSuper(obj, args);
		//执行action之后执行interceptor
		interceptor(actionBean.getActionInterceptors(), false, 
				(HttpServletRequest)args[0], null, actionBean.getResultName(result));
		return result;
	}
	
	//执行interceptor
	private void interceptor(LinkedList<InterceptorBean> actionInterceptors,
			boolean isPre, HttpServletRequest req, String actionName, String actionResult) {
		System.out.println("\nCall intercept " + (isPre?"pre": "after") + " ...");
		if(isPre) {//根据isPro的值判断是执行action还是执行action之后
			int count = 0;
			//执行每一个interceptor
			for(InterceptorBean i: actionInterceptors) {
				String className = i.getInterceptorClass();
				String methodName = i.getInterceptorPredo();
				System.out.println(count++ + ".Call " + i.getInterceptorName() + 
						":" + className + "." + methodName);
				try {
					//使用Java反射机制调用类的方法
					Class<?> c = Class.forName(className);
					Method m = c.getDeclaredMethod(methodName, 
							HttpServletRequest.class, String.class);
					m.invoke(c.newInstance(), req, actionName);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else {
			int count = 0;
			while(!actionInterceptors.isEmpty()) {
				//反向获取并执行每一个interceptor
				InterceptorBean i = actionInterceptors.pollLast();
				String className = i.getInterceptorClass();
				String methodName = i.getInterceptorAfterdo();
				System.out.println(count++ + ".Call " + i.getInterceptorName() + 
						":" + className + "." + methodName);
				try {
					//使用Java反射机制调用类的方法
					Class<?> c = Class.forName(className);
					Method m = c.getDeclaredMethod(methodName, 
							HttpServletRequest.class, String.class);
					m.invoke(c.newInstance(), req, actionResult);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("intercept " + (isPre?"pre": "after") + " back!\n");
	}

}
