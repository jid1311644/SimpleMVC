package sc.ustc.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.SkipPageException;

import sc.ustc.tools.XMLTool;

public class SimpleController extends HttpServlet {
	
	private static final JspFactory jspFactory = JspFactory.getDefaultFactory();

	@Override
	public void doGet(HttpServletRequest req,
			HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	public void doPost(HttpServletRequest req, 
			HttpServletResponse resp)
			throws ServletException, IOException {
		
		//设置界面格式
		resp.setContentType("text/html; charset=GBK");
		resp.setCharacterEncoding("utf-8");
		req.setCharacterEncoding("utf-8");
/*		
		String actionName = req.getServletPath().toString();
		String[] actionUrl = actionName.split("/");
		actionName = actionUrl[actionUrl.length - 1].substring(0, actionName.indexOf("."));
		String path = this.getServletContext().getRealPath("WEB-INF/classes/controller.xml");
		Map<String, String> actionMap = new XMLTool().readXML(actionName, path);
		if(!actionMap.isEmpty()) {
			String className = actionMap.get("class");
			String methodName = actionMap.get("method");
			try {
				Class<?> c = Class.forName(className);
				Method m = c.getDeclaredMethod(methodName,
						HttpServletRequest.class, HttpServletResponse.class);
				String result = (String) m.invoke(c.newInstance(), req, resp);
				String resName = actionMap.get("result" + result);
				String resType = resName.substring(0, resName.indexOf("-"));
				String resValue = resName.substring(resName.indexOf("-") + 1);
				
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
		}
		else {
			resp.sendRedirect("/UseSC/jsp/login.jsp");
		}*/

		final PageContext pageContext;
		JspWriter out = null;
		
		PageContext jspPageContent = null;
		JspWriter jspOut = null;
		
		try {
			//获取界面
			pageContext = jspFactory.getPageContext(this, req, resp, "",
					true, 8192, true);
			jspPageContent = pageContext;
			out = pageContext.getOut();
			jspOut = out;
			//设置响应输出
			out.write("<html>\r\n"
				+ "  <head>\r\n"
				+ "    <title>SimpleController</title>\r\n"
				+ "  </head>\r\n"
				+ "  <body>欢迎使用SimpleController!</body>\r\n"
				+ "</html>\r\n");
		} catch (Throwable t) {
			// TODO: handle exception
			if(!(t instanceof SkipPageException)) {
				out = jspOut;
				if(out != null && out.getBufferSize() != 0) {
					try {
						out.clearBuffer();
					} catch (IOException e) {
						// TODO: handle exception
					}
				}
				if(jspPageContent != null) {
					jspPageContent.handlePageException(t);
				}
				else {
					throw new ServletException(t);
				}
			}
		}finally {
			jspFactory.releasePageContext(jspPageContent);
		}
		
	}

	
}
