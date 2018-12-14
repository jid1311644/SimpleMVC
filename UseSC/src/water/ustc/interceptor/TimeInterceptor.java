package water.ustc.interceptor;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

public class TimeInterceptor {
	
	public void preAction(HttpServletRequest req, String actionName) {
		System.out.println("Call TimeInterceptor.preAction ...");
		//��¼��ǰactionִ��ǰ��ʱ��
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		String time;
		System.out.println("preAction-time:" + (time = df.format(new Date()))); 
		LogInterceptor.setStartTime(time);
		System.out.println("TimeInterceptor.preAction back!");
	}
	
	public void afterAction(HttpServletRequest req, String actionResult) {
		System.out.println("Call TimeInterceptor.afterAction ...");
		//��¼��ǰactionִ�к��ʱ��
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		String time;
		System.out.println("afterAction-time:" + (time = df.format(new Date()))); 
		LogInterceptor.setEndTime(time);
		System.out.println("TimeInterceptor.afterAction back!");
	}

}
