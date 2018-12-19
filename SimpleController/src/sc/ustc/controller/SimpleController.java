package sc.ustc.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.cglib.proxy.Enhancer;
import sc.ustc.beans.ActionBean;
import sc.ustc.beans.InterceptorBean;
import sc.ustc.tools.CglibProxy;
import sc.ustc.tools.XMLTool;
import sc.ustc.tools.XMLbyDOM;

public class SimpleController extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req,
			HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("This is doGet. Call doPost ...");
		doPost(req, resp);
	}
	
	@Override
	public void doPost(HttpServletRequest req, 
			HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("This is doPost.");
		//���ý����ʽ
		resp.setContentType("text/html; charset=GBK");
		resp.setCharacterEncoding("utf-8");
		req.setCharacterEncoding("utf-8");

		//����servlet��action��controller.xml��Ŀ¼
		String actionName = req.getServletPath().toString();
		String[] actionUrl = actionName.split("/");
		actionName = actionUrl[actionUrl.length - 1];
		actionName = actionName.substring(0, actionName.indexOf("."));
		System.out.println("\nactionName:" + actionName);
		String path = this.getServletContext().getRealPath("WEB-INF/classes/controller.xml");
		System.out.println("path:" + path + "\n");
		
		//����readInterceptor����ʵ�ֶ��������Ľ���
		LinkedList<InterceptorBean> interceptorBeans = new XMLTool().readInterceptor(path);
		int count = 0;
		System.out.println("readInterceptor result:");
		for(InterceptorBean i: interceptorBeans) {
			System.out.println("interceptorBeans-" + (count++));
			i.display();
		}
		System.out.println();
		
		//����readAction����ʵ�ֶ�action�������ݵĽ���
		ActionBean actionBean = new XMLTool().readAction(actionName, path);
		System.out.println("readAction result:");
		actionBean.display();
		
		//��ȡaction�е�����������
		LinkedList<InterceptorBean> actionInterceptors = new LinkedList<>();
		for(String interceptorName: actionBean.getActionInterceptorsName()) {
			searchInterceptor(interceptorBeans, actionInterceptors, interceptorName);
		}
		actionBean.setActionInterceptors(actionInterceptors);
		//ִ��action
		action(actionBean, req, resp);
		
		System.out.println("doPost back!");
		
	}
	
	//ִ��action
	private void action(ActionBean actionBean, 
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String className = actionBean.getClassName();
		String methodName = actionBean.getMethodName();
		System.out.println("Call action:" + actionBean.getActionName() + 
				" " + className + "." + methodName);
		try {
			//��ȡ��ͷ���
			Class<?> c = Class.forName(className);
			Method m = c.getDeclaredMethod(methodName,
					HttpServletRequest.class, 
					HttpServletResponse.class);
			
			//LogoutAction��ִ��
			if(actionBean.getActionName().equals("logout") || 
					actionBean.getActionName().equals("viewTest")) {
				m.invoke(c.newInstance(), req, resp);
			}
			//����action��ִ��
			else {
				//����Enhancer���ɴ�����
				Enhancer enhancer = new Enhancer();
				enhancer.setSuperclass(c);
				enhancer.setCallback(new CglibProxy(actionBean));
				Object cl = enhancer.create();
				//ʵ�ִ�����
				String result = (String) m.invoke(cl, req, resp);
				String resName = actionBean.getResultName(result);
				String resType = actionBean.getResultType(result);
				String resValue = actionBean.getResultValue(result);
				
				//�����õ�����ֵresult�ľ������ݣ�����valueֵ����ҳ����ת
				System.out.println("Action result:\nresult-name=" + resName
						+ "\nresult-type=" + resType + "\nresult-value=" + resValue);
				if(resType.equals("foward")) {
					if(resValue.endsWith("_view.xml")) {
						//��Ҫ�������ͼ
						System.out.println("\nController loading view ...");
						String viewPath = this.getServletContext().getRealPath("views/" + resValue);;
						System.out.println("path:" + viewPath);
						String jsp = new XMLbyDOM().readView(viewPath);
						System.out.println(jsp);
						//������ͼ
						PrintWriter out = resp.getWriter();
						out.write(jsp);
					}
					else {
						req.getRequestDispatcher(resValue).forward(req, resp);
					}
				}
				else if(resType.equals("redirect")) {
					resp.sendRedirect(resValue);
				}
				System.out.println("action back:" + actionBean.getActionName() + "\n");
			}
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
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
		}
		catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//�ڸ���action�����õ�����������Ѱ�Ҷ�Ӧ������
	private void searchInterceptor(
			LinkedList<InterceptorBean> allInterceptors, 
			LinkedList<InterceptorBean> actionInterceptors, String name) {
		boolean inStack = true;
		for(InterceptorBean i: allInterceptors) {
			if(i.getInterceptorName().equals(name)) {
				actionInterceptors.add(i);
				inStack = false;
				break;
			}
		}
		if(inStack) {
			for(InterceptorBean i: allInterceptors) {
				if(i.getInterceptorStack().equals(name)) {
					actionInterceptors.add(i);
				}
			}
		}
	}
	
}
