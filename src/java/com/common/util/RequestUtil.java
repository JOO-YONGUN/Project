package com.common.util;

import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

/**
 * <code>RequestUtil</code>
 * 
 * @version $Revision: 0.1 $ $Date: 0000-00-00 00:00:00 $
 * @author 
 * @history
 */
public class RequestUtil {

	/**
     * 초기화 처리
     * 
     * @param request ServletRequest 객체
     * @return HashMap map
     */
	public static HashMap getRequestMap(HttpServletRequest request) {
		
		HashMap returnMap = new HashMap();
		Enumeration enumeration = request.getParameterNames();
		
		try {
			
			while(enumeration.hasMoreElements()) {
				String paramName = (String) enumeration.nextElement();
				String [] paramValue = request.getParameterValues(paramName);
				int paramValueLength = paramValue.length;
				
				if (paramValueLength > 1) {
					returnMap.put(paramName, paramValue);
				}
				else {
					returnMap.put(paramName, paramValue[0]);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
				
		return returnMap;
	}
	
	/**
	 * 모바일 여부
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isMobile(HttpServletRequest request) {
		
		String userAgent = request.getHeader("user-agent");
		boolean mobileFlag1 = userAgent.matches(".*(iPhone|iPod|Android|Windows CE|BlackBerry|Symbian|Windows Phone|webOS|Opera Mini|Opera Mobil|POLARIS|IEMobile|Igtelecom|nokia|SonyEricsson).*");
		boolean mobileFlag2 = userAgent.matches(".*(LG|SAMSUNG|Samsung).*");
		
		if (mobileFlag1 || mobileFlag2) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
}
