package sc.ustc.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import sc.ustc.beans.ActionBean;
import sc.ustc.beans.InterceptorBean;

public class XMLTool {
	
	//解析Interceptor
	public LinkedList<InterceptorBean> readInterceptor(String path) 
			throws FileNotFoundException {
		System.out.println("Call XMLTool.readInterceptor");
		//字节流读入
		BufferedReader br = new BufferedReader(new FileReader(new File(path)));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		LinkedList<InterceptorBean> beans = new LinkedList<>();
		String[] inters = sb.toString().split("<interceptor ");
		for(int i = 1; i < inters.length; i++) {
			//解析每个Interceptor
			String interceptorName = inters[i].substring(inters[i].indexOf("name=\"") + 6);
			interceptorName = interceptorName.substring(0, interceptorName.indexOf("\""));
			String interceptorClass = inters[i].substring(inters[i].indexOf("class=\"") + 7);
			interceptorClass = interceptorClass.substring(0, interceptorClass.indexOf("\""));
			String interceptorPredo = inters[i].substring(inters[i].indexOf("predo=\"") + 7);
			interceptorPredo = interceptorPredo.substring(0, interceptorPredo.indexOf("\""));
			String interceptorAfterdo = inters[i].substring(inters[i].indexOf("afterdo=\"") + 9);
			interceptorAfterdo = interceptorAfterdo.substring(0, interceptorAfterdo.indexOf("\""));
			beans.add(new InterceptorBean(interceptorName, interceptorClass, interceptorPredo, interceptorAfterdo));
		}
		System.out.println("XMLTool.readInterceptor back!");
		return beans;
	}

	//解析Action
	public ActionBean readAction(String action, String path) 
			throws FileNotFoundException {
		System.out.println("Call XMLTool.readAction");
		//字节流读入
		BufferedReader br = new BufferedReader(new FileReader(new File(path)));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		String[] sl = sb.toString().split("</action>");
		for(int i = 0; i < sl.length; i++) {
			//解析action名，如果与用户输入的action符合继续进行解析
			String acName = sl[i].substring(sl[i].indexOf("<action name=\"") + 14);
			if(acName.startsWith(action + "\"")) {
				//解析类名
				String className = acName.substring(acName.indexOf("class=\"") + 7);
				className = className.substring(0, className.indexOf("\""));
				//解析方法名
				String methodName = acName.substring(acName.indexOf("method=\"") + 8);
				methodName = methodName.substring(0, methodName.indexOf("\""));
				//bean初始化
				ActionBean actionBean = new ActionBean(action, className, methodName);
				//解析拦截器
				String[] interceptors = acName.split("<interceptro-ref name=\"");
				for(int j = 1; j < interceptors.length; j++) {
					String interceptroRefName = interceptors[j].substring(0, interceptors[j].indexOf("\""));
					actionBean.addActionInterceptors(interceptroRefName);
				}
				//解析result
				String[] result = acName.split("<result");
				for(int j = 1; j < result.length; j++) {
					String resName = result[j].substring(result[j].indexOf("name=\"") + 6);
					resName = resName.substring(0, resName.indexOf("\""));
					String resType = result[j].substring(result[j].indexOf("type=\"") + 6);
					resType = resType.substring(0, resType.indexOf("\""));
					String resValue = result[j].substring(result[j].indexOf("value=\"") + 7);
					resValue = resValue.substring(0, resValue.indexOf("\""));
					//bean完善
					if(resName.equals("ok")) {
						actionBean.setResultOK(resName, resType, resValue);
					}
					else if(resName.equals("error")){
						actionBean.setResultError(resName, resType, resValue);
					}
				}
				System.out.println("XMLTool.readXML back!");
				return actionBean;
			}
		}
		System.out.println("XMLTool.readXML back!");
		return null;
	}
	
	//解析Interceptor
	public InterceptorBean readInterceptor() {
		return null;
	}
	
}
