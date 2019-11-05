package com.common;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <code>LogFactory</code>
 * 
 * @version $Revision: 0.1 $ $Date: 0000-00-00 00:00:00 $
 * @author 
 * @history
 */
@SuppressWarnings("serial")
@Resource(name = "logFactory")
public class LogFactory implements Serializable {

	private static final Logger logger = LoggerFactory.getLogger(LogFactory.class);
	private StringBuffer logBuffer = new StringBuffer();
	private Long startTime = 0L;
	private Long endTime = 0L;
	private HttpServletRequest request;
	private Properties configProperties;

	/**
	 * init
	 * 
	 * @param request
	 * @param configProperties
	 */
	public LogFactory(HttpServletRequest request,Properties configProperties) {
		if ( configProperties != null && request != null) {
			this.request = request;
			this.configProperties = configProperties;
			
			Calendar cal = Calendar.getInstance();
			startTime = cal.getTimeInMillis();
			
			logInit();
		}
	}

	/**
	 * 로그파일 작성
	 */
	public void logWrite() {
		//END TIME
		Calendar cal = Calendar.getInstance();
		endTime = cal.getTimeInMillis();
		// PROCESS TIME
		logBuffer.append("-------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
		logBuffer.append((new StringBuilder()).append("(*) Processing-Time : ").append(getProcessTime(startTime,endTime)).append("\n").toString());
		logBuffer.append("-------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
		
		logger.info(logBuffer.toString());
		//System.out.println("test:"+logBuffer.toString());
		//메모리 제거
		logBuffer.setLength(0);
		logBuffer = null;
	}
	
	/**
	 * 초기화 
	 */
	public void logInit() {
		logBuffer.append((new StringBuilder()).append("\n"));
		logBuffer.append("-------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
		logBuffer.append((new StringBuilder()).append("(*) Accepted : ").append(nowDatetime()).append(" , ver [ ").append(configProperties.getProperty("service.version")).append(" ]").append("\n").toString());
		logBuffer.append("-------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
		// URI
		logBuffer.append((new StringBuilder()).append("(*) ((((( ").append(request.getRequestURI()).append(" ))))) called ...").append("\n").toString());
				
		// PARAMETER
		Enumeration<?> enumeration = request.getParameterNames();
		HashMap<String, Object> param = new HashMap<String, Object>();
		while(enumeration.hasMoreElements()){
			String key = (String) enumeration.nextElement();
			String[] values = request.getParameterValues(key);
			param.put(key, (values.length > 1) ? values:values[0]);
			logBuffer.append((new StringBuilder()).append("(P) " + key + "  \t: " + values[0] + "\n"));
		}
		logBuffer.append((new StringBuilder()).append("\n"));
	}

	/**
	 * log 추가
	 * 
	 * @param s
	 */
	public void logTrace(String s){
		if(s != null){
			logBuffer.append((new StringBuilder()).append(s+"\n"));			
		}else{
			logBuffer.append((new StringBuilder()).append("null \n"));
		}
	}
	
	/**
	 * return code set
	 * @param ctx
	 */
	public void logResultCode(ConcurrentHashMap<String, Object> ctx){
		logBuffer.append((new StringBuilder()).append("(*) Result-Processing .."+"\n"));
		logBuffer.append((new StringBuilder()).append("     Result-Cd 		: " + ctx.get("resp_cd")+"\n"));
		logBuffer.append((new StringBuilder()).append("     Result-Msg 		: " + ctx.get("respMsg")+"\n"));
		logBuffer.append((new StringBuilder()).append("     Result_Detail 	: " + ctx.get("resp_msg_detail")+"\n"));
	}
	
	/**
	 * 현재 시간
	 * @return
	 */
	public String nowDatetime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();

		return format.format(calendar.getTime());
	}
	
	/**
	 * Process time 계산
	 * @return
	 */
	public static String getProcessTime(long endTime,long startTime){		
		Date proccessingdt = new Date(startTime - endTime);
		Calendar cal = Calendar.getInstance();
		cal.setTime(proccessingdt);
		String sProcessTime = (new StringBuilder()).append(cal.get(12)).append("m ").append(cal.get(13)).append("s ").append(cal.get(14)).append("ms").toString();
		
		return sProcessTime;
	}
   
	/**
	 * 로그파일 작성 debug 레벨
	 */
	public void logWriteDebug(String msg) {
		logger.debug("debug : "+msg);
	}	
	
	/**
	 * 로그파일 작성 info 레벨
	 */
	public void logWriteInfo(String msg) {
		logger.info("info : "+msg);
	}
	
	/**
	 * 로그파일 작성 error 레벨
	 */
	public void logWriteError(String msg) {
		logBuffer.append(msg);
		logger.error("error : "+msg);
	}
}
