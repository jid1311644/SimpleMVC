package water.ustc.interceptor;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import water.ustc.db.Log;


public class LogInterceptor {
	
	static private String name;
	static private String startTime;
	static private String endTime;
	static private String result;
	
	public void preAction(HttpServletRequest req, String actionName) {
		System.out.println("Call LogInterceptor.preAction ...");
		//记录当前action的名字
		name = actionName;
		System.out.println("LogInterceptor.preAction back!");
	}
	
	public void afterAction(HttpServletRequest req, String actionResult) {
		System.out.println("Call LogInterceptor.afterAction ...");
		//记录当前action的结果
		result = actionResult;
		//生成log并保存
		Log l = new Log(name, startTime, endTime, result);
		System.out.println(Log.add(l)?
				"add success!": "add failure!");
		System.out.println("LogInterceptor.afterAction back!");
	}

	static public void setStartTime(String time) {
		startTime = time;
	}

	static public void setEndTime(String time) {
		endTime = time;
	}

}
