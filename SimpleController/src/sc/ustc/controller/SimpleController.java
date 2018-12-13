package sc.ustc.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.SkipPageException;

import sc.ustc.beans.ActionBean;
import sc.ustc.beans.InterceptorBean;
import sc.ustc.tools.XMLTool;

public class SimpleController extends HttpServlet {
	
	private static final JspFactory jspFactory = JspFactory.getDefaultFactory();

	@Override
	public void doGet(HttpServletRequest req,
			HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("This is doGet. Call doPost..");
		doPost(req, resp);
	}
	
	@Override
	public void doPost(HttpServletRequest req, 
			HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("This is doPost. Setting type..");
		//���ý����ʽ
		resp.setContentType("text/html; charset=GBK");
		resp.setCharacterEncoding("utf-8");
		req.setCharacterEncoding("utf-8");

		//����servlet��action��controller.xml��Ŀ¼
		String actionName = req.getServletPath().toString();
		String[] actionUrl = actionName.split("/");
		actionName = actionUrl[actionUrl.length - 1];
		actionName = actionName.substring(0, actionName.indexOf("."));
		System.out.println("actionName:" + actionName);
		String path = this.getServletContext().getRealPath("WEB-INF/classes/controller.xml");
		System.out.println("path:" + path);
		
		//����readInterceptor����ʵ�ֶ��������Ľ���
		LinkedList<InterceptorBean> interceptorBeans = new XMLTool().readInterceptor(path);
		int count = 0;
		System.out.println();
		for(InterceptorBean i: interceptorBeans) {
			System.out.println("interceptorBeans-" + (count++));
			i.display();
		}
		System.out.println();
		
		//����readAction����ʵ�ֶ�action�������ݵĽ���
		ActionBean actionBean = new XMLTool().readAction(actionName, path);
		actionBean.display();
		
		String className = actionBean.getClassName();
		String methodName = actionBean.getMethodName();
		try {
			//��ȡ�����ͷ�������ͨ��invoke��������ʵ�ָ÷����õ�����ֵresult
			Class<?> c = Class.forName(className);
			Method m = c.getDeclaredMethod(methodName,
					HttpServletRequest.class, HttpServletResponse.class);
			String result = (String) m.invoke(c.newInstance(), req, resp);
			String resName = actionBean.getResultName(result);
			String resType = actionBean.getResultType(result);
			String resValue = actionBean.getResultValue(result);
			
			//�����õ�����ֵresult�ľ������ݣ�����valueֵ����ҳ����ת
			System.out.println("action-result: name=" + resName + " type=" + resType + " value=" + resValue);
			if(resType.equals("foward")) {
				req.getRequestDispatcher(resValue).forward(req, resp);
			}
			else if(resType.equals("redirect")) {
				resp.sendRedirect(resValue);
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
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("doPost back!");
		
	}

	
}
