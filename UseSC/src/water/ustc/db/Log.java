package water.ustc.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Log {
	
	private String name;
	private String startTime;
	private String endTime;
	private String result;
	
	public Log(String name, String startTime, String endTime, String result) {
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.result = result;
	}
	
	//add
	static public boolean add(Log log) {
		boolean f = true;
		System.out.println("Call Log.add ...");
		try {
			File file = new File("D:/JavaProject/SimpleMVC/"
					+ "SimpleController/logs/log.xml");
			StringBuffer sb = new StringBuffer();
			if(file.exists()) {//如果已有log文件，读取其内容
				FileInputStream fis = new FileInputStream(file);
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(isr);
				sb = new StringBuffer();
				String line;
				while((line = br.readLine()) != null) {
					sb.append(line + "\r\n");
				}
				br.close();
				isr.close();
				fis.close();
				file.delete();
			}
			else {//否则新建log文件
				sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
						"<log>\r\n" + 
						"	<!-- other actions -->\r\n" + 
						"</log>");
			}
			//编辑log内容
			String str = sb.toString();
			str = str.substring(0, str.lastIndexOf("	<!-- other actions -->"));
			sb = new StringBuffer(str);
			sb.append("	<action>\r\n"
					+ "		<name>" + log.name + "</name>\r\n"
					+ "		<s-time>" + log.startTime + "</s-time>\r\n"
					+ "		<e-time>" + log.endTime + "</e-time>\r\n"
					+ "		<result>" + log.result + "</result>\r\n"
					+ "	</action>\r\n");
			sb.append("	<!-- other actions -->\r\n</log>\r\n");
			//保存log文件
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			PrintWriter pw = new PrintWriter(fos);
			pw.write(sb.toString());
			pw.flush();
			pw.close();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			f = false;
		}
		return f;
	}

}
