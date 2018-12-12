package sc.ustc.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class XMLTool {

	//解析XML文件
	public Map<String, String> readXML(String action, String path) 
			throws FileNotFoundException {
		//字节流读入
		BufferedReader br = new BufferedReader(
				new FileReader(new File(path)));
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
		Map<String, String> map = new HashMap<>();
		for(int i = 0; i < sl.length; i++) {
			//解析Action名
			String acName = sl[i].substring(sl[i].indexOf("name=\"") + 6);
			if(acName.startsWith(action + "\"")) {
				map.put("action", action);
				//解析类名
				String className = acName.substring(acName.indexOf("class=\"") + 7);
				map.put("class", className.substring(0, className.indexOf("\"")));
				//解析方法名
				String methodName = className.substring(className.indexOf("method=\"") + 8);
				map.put("method", methodName.substring(0, methodName.indexOf("\"")));
				//解析result
				String[] result = methodName.split("<result");
				for(int j = 0; j < result.length; j++) {
					String resName = result[j].substring(result[j].indexOf("name=\"") + 6);
					String resType = resName.substring(resName.indexOf("type=\"") + 6);
					String resValue = resType.substring(resType.indexOf("value=\"") + 7);
					String res = resType.substring(0, resType.indexOf("\"")) + "-"
							+ resValue.substring(0, resValue.indexOf("\""));
					map.put("result" + resName.substring(0, resName.indexOf("\"")), res);
				}
			}
		}
		return map;
	}
	
}
