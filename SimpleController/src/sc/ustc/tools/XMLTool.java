package sc.ustc.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import sc.ustc.beans.ActionBean;
import sc.ustc.beans.InterceptorBean;
import sc.ustc.di.DiBean;
import sc.ustc.di.DiField;

public class XMLTool {
	
	private StringBuilder sb;
	
	public XMLTool(String path) {
		// TODO Auto-generated constructor stub
		//�ֽ�������
		sb = new StringBuilder();
		String line = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(path)));
			while((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	//����Interceptor
	public LinkedList<InterceptorBean> readInterceptor() {
		System.out.println("Call XMLTool.readInterceptor ...");
		//��������������ȡ����������XML����
		LinkedList<InterceptorBean> beans = new LinkedList<>();
		String interceptorXML = sb.toString();
		interceptorXML = interceptorXML.substring(0, interceptorXML.indexOf("</interceptors>") + 15);
		//������������ջ�����
		Map<String, String> interceptorStack = new HashMap<>();
		if(interceptorXML.indexOf("<interceptor-stack") != -1) {
			String[] stacks = interceptorXML.split("<interceptor-stack name=\"");
			for(int i = 1; i < stacks.length; i++) {
				String[] inters = stacks[i].split("<interceptro-ref name=\"");
				String stackName = inters[0].substring(0, inters[0].indexOf("\""));
				for(int j = 1; j < inters.length; j++) {
					String interName = inters[j].substring(0, inters[j].indexOf("\""));
					interceptorStack.put(interName, stackName);
				}
			}
		}
		//������������ע�Ჿ�ִ���
		String[] inters = sb.toString().split("<interceptor ");
		for(int i = 1; i < inters.length; i++) {
			//����ÿ��Interceptor
			String interceptorName = inters[i].substring(inters[i].indexOf("name=\"") + 6);
			interceptorName = interceptorName.substring(0, interceptorName.indexOf("\""));
			String interceptorClass = inters[i].substring(inters[i].indexOf("class=\"") + 7);
			interceptorClass = interceptorClass.substring(0, interceptorClass.indexOf("\""));
			String interceptorPredo = inters[i].substring(inters[i].indexOf("predo=\"") + 7);
			interceptorPredo = interceptorPredo.substring(0, interceptorPredo.indexOf("\""));
			String interceptorAfterdo = inters[i].substring(inters[i].indexOf("afterdo=\"") + 9);
			interceptorAfterdo = interceptorAfterdo.substring(0, interceptorAfterdo.indexOf("\""));
			String stackName;
			if((stackName = interceptorStack.get(interceptorName)) != null) {
				beans.add(new InterceptorBean(interceptorName, interceptorClass, interceptorPredo, interceptorAfterdo, stackName));
			}
			else {
				beans.add(new InterceptorBean(interceptorName, interceptorClass, interceptorPredo, interceptorAfterdo, "null"));
			}
		}
		System.out.println("XMLTool.readInterceptor back!");
		return beans;
	}

	//����Action
	public ActionBean readAction(String action) {
		System.out.println("Call XMLTool.readAction ...");
		String[] sl = sb.toString().split("</action>");
		for(int i = 0; i < sl.length; i++) {
			//����action����������û������action���ϼ������н���
			String acName = sl[i].substring(sl[i].indexOf("<action name=\"") + 14);
			if(acName.startsWith(action + "\"")) {
				//��������
				String className = acName.substring(acName.indexOf("class=\"") + 7);
				className = className.substring(0, className.indexOf("\""));
				//����������
				String methodName = acName.substring(acName.indexOf("method=\"") + 8);
				methodName = methodName.substring(0, methodName.indexOf("\""));
				//bean��ʼ��
				ActionBean actionBean = new ActionBean(action, className, methodName);
				//����������
				String[] interceptors = acName.split("<interceptro-ref name=\"");
				for(int j = 1; j < interceptors.length; j++) {
					String interceptroRefName = interceptors[j].substring(0, interceptors[j].indexOf("\""));
					actionBean.addActionInterceptorRefs(interceptroRefName);
				}
				//����result
				String[] result = acName.split("<result");
				for(int j = 1; j < result.length; j++) {
					String resName = result[j].substring(result[j].indexOf("name=\"") + 6);
					resName = resName.substring(0, resName.indexOf("\""));
					String resType = result[j].substring(result[j].indexOf("type=\"") + 6);
					resType = resType.substring(0, resType.indexOf("\""));
					String resValue = result[j].substring(result[j].indexOf("value=\"") + 7);
					resValue = resValue.substring(0, resValue.indexOf("\""));
					//bean����
					if(resName.equals("ok")) {
						actionBean.setResultOK(resName, resType, resValue);
					}
					else if(resName.equals("error")){
						actionBean.setResultError(resName, resType, resValue);
					}
				}
				System.out.println("XMLTool.readAction back!");
				return actionBean;
			}
		}
		System.out.println("XMLTool.readAction back!");
		return null;
	}
	
	//��������ע���ļ�di.xml
	public DiBean readDi(String beanID, String beanClass) {
		System.out.println("Call XMLTool.readDi ...");
		System.out.println("beanID:" + beanID + "	beanClass:" + beanClass);
		DiBean diBean = null;
		String[] beans = sb.toString().split("<bean ");
		String bean = null;
		boolean f = false;
		//����di��bean����class����di�ļ�
		for(int i = 1; i < beans.length; i++) {
			bean = beans[i].substring(0, beans[i].indexOf("</bean>"));
			if(beanID != null) {
				if(bean.indexOf("id=\"" + beanID + "\"") != -1) {
					f = true;
					break;
				}
			}
			else if(beanClass != null) {
				if(bean.indexOf("class=\"" + beanClass + "\"") != -1) {
					f = true;
					break;
				}
			}
		}
		if(f) {
			String id = bean.substring(bean.indexOf("id=\"") + 4);
			id = id.substring(0, id.indexOf("\""));
			String clasS = bean.substring(bean.indexOf("class=\"") + 7);
			clasS = clasS.substring(0, clasS.indexOf("\""));
			if(!id.equals("") && !clasS.equals("")) {
				diBean = new DiBean(id, clasS);
				//�ж��Ƿ���ڵ㣬��û�����������������
				if(beanID == null && beanClass != null) {
					diBean.setParentBean(null);
				}
				String[] fields = bean.split("<field ");
				for(int i = 1; i < fields.length; i++) {
					String field = fields[i].substring(0, fields[i].indexOf("</field>"));
					String name = field.substring(field.indexOf("name=\"") + 6);
					name = name.substring(0, name.indexOf("\""));
					String beanRef = field.substring(field.indexOf("bean-ref=\"") + 10);
					beanRef = beanRef.substring(0, beanRef.indexOf("\""));
					diBean.addDiField(new DiField(name, beanRef));
				}
			}
		}
		System.out.println("XMLTool.readDi back!");
		return diBean;
	}
	
}
