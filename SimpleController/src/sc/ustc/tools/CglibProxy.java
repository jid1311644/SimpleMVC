package sc.ustc.tools;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import sc.ustc.beans.ActionBean;
import sc.ustc.beans.InterceptorBean;
import sc.ustc.di.DiBean;

public class CglibProxy implements MethodInterceptor {
	
	private ActionBean actionBean;
	public CglibProxy(ActionBean bean) {
		// TODO Auto-generated constructor stub
		this.actionBean = bean;
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		// TODO Auto-generated method stub
		//ִ��action֮ǰִ��interceptor
		interceptor(actionBean.getActionInterceptors(), true, 
				(HttpServletRequest)args[0], (HttpServletResponse)args[1], 
				actionBean.getActionName(), null);
		//ִ��action
		//��������ע�������ļ�
		System.out.println("Search " + actionBean.getActionName() + " in di.xml ...");
		DiBean bean = new DiBean();
		LinkedList<DiBean> diTree = bean.createDiTree(actionBean.getClassName());
		String result = null;
		if(!diTree.isEmpty()) {
			System.out.println("Found:\r\n" + bean.display(diTree) + "\r\n");
			Object o = introspector(diTree);
			Method m = o.getClass().getMethod("handleLogin", 
					HttpServletRequest.class, HttpServletResponse.class);
			result = (String)m.invoke(o, (HttpServletRequest)args[0], (HttpServletResponse)args[1]);
		}
		else {
			System.out.println("Not found!");
			result = (String)proxy.invokeSuper(obj, args);
		}
		//ִ��action֮��ִ��interceptor
		interceptor(actionBean.getActionInterceptors(), false, 
				(HttpServletRequest)args[0], (HttpServletResponse)args[1], 
				null, actionBean.getResultName(result));
		return result;
	}

	//ִ��interceptor
	private void interceptor(LinkedList<InterceptorBean> actionInterceptors,
			boolean isPre, HttpServletRequest req,  HttpServletResponse resp,
			String actionName, String actionResult) {
		System.out.println("\nCall intercept " + (isPre?"pre": "after") + " ...");
		if(isPre) {//����isPro��ֵ�ж���ִ��action����ִ��action֮��
			int count = 0;
			//ִ��ÿһ��interceptor
			for(InterceptorBean i: actionInterceptors) {
				String className = i.getInterceptorClass();
				String methodName = i.getInterceptorPredo();
				System.out.println(count++ + ".Call " + i.getInterceptorName() + 
						":" + className + "." + methodName);
				try {
					//ʹ��Java������Ƶ�����ķ���
					Class<?> c = Class.forName(className);
					Method m = c.getDeclaredMethod(methodName, 
							HttpServletRequest.class, HttpServletResponse.class, String.class);
					m.invoke(c.newInstance(), req, resp, actionName);
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
				//�����ȡ��ִ��ÿһ��interceptor
				InterceptorBean i = actionInterceptors.pollLast();
				String className = i.getInterceptorClass();
				String methodName = i.getInterceptorAfterdo();
				System.out.println(count++ + ".Call " + i.getInterceptorName() + 
						":" + className + "." + methodName);
				try {
					//ʹ��Java������Ƶ�����ķ���
					Class<?> c = Class.forName(className);
					Method m = c.getDeclaredMethod(methodName, 
							HttpServletRequest.class, HttpServletResponse.class, String.class);
					m.invoke(c.newInstance(), req, resp, actionResult);
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

	//ִ����ʡ����
	private Object introspector(LinkedList<DiBean> tree) {
		// TODO Auto-generated method stub
		System.out.println("\nCall introspector ...");
		try {
			//�ѵ�ǰaction������ϵ�漰����������ͳһʵ����
			HashMap<String, Object> instanceMap = new HashMap<>();
			Class<?> c;
			for(DiBean db: tree) {
				c = Class.forName(db.getClasS());
				instanceMap.put(db.getID(), c.newInstance());
			}
			//ʹ��Java��ʡ���Ƶ������Ե�setter����
			DiBean root =  tree.pollFirst();
			while(!tree.isEmpty()) {
				DiBean db = tree.pollLast();
				Object current = instanceMap.get(db.getID());
				Object parent = instanceMap.get(db.getParentBean().getID());
				System.out.println("Current property:" + db.getFieldName());
				System.out.println("Depend class:" + db.getParentBean().getClasS());
				PropertyDescriptor pd = new PropertyDescriptor(db.getFieldName(), parent.getClass());
				Method m = pd.getWriteMethod();
				m.invoke(parent, current);
			}
			return instanceMap.get(root.getID());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
