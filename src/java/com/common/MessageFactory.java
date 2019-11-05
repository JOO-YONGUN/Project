package com.common;

import java.util.HashMap;

/**
 * <code>MessageFactory</code>
 * 
 * @version $Revision: 0.1 $ $Date: 2000-00-00 00:00:00 $
 * @author 
 * @history
 */
public class MessageFactory {

	// message code
	public static final String CRLF = "\n";
	public static final String LEGACY_RESULTMSG_DELIMETER = "::::";
	public static final String OK = "200";
	
	public static final String NOT_AUTH = "401";
	public static final String ERROR = "500";
	public static final String ERROR_ALP_HTTPS = "501";

	public static final String PARAMETERS_INVALID = "400";
	public static final String UNKNOWN_EXCEPTION = "999";
	public static final String LEGACY_API_SERVER_ERROR = "550";

	// message map
	static HashMap<String, String> msg = null;

	static HashMap<String, String> getMessageList() {
		msg = new HashMap<String, String>();
		msg.put("200", "OK");
		msg.put("400", "�߸��� �Ķ�����Դϴ�.");
		msg.put("999", "�˼����� �����Դϴ�.");
		msg.put("401", "������ �ʿ��մϴ�.");
		msg.put("500", "���� �����Դϴ�.");
		msg.put("501", "���� �����Դϴ�.");
		msg.put("550", "���� ����� �������� �ʽ��ϴ�.");

		return msg;
	}

	static String makeMessages(String code, String sMsg) {
		String returnMsg = null;
		if(msg == null){
			msg = getMessageList();
		}
		returnMsg = (msg.get(code) == null) ? (String) msg.get("999") : (String) msg.get(code);
		returnMsg = sMsg == null || "".equals(sMsg) ? returnMsg
				: (returnMsg = (new StringBuilder()).append(returnMsg).append(" (").append(sMsg).append(")")
						.toString());
		return returnMsg;
	}

	public static String make(String code) {
		return makeMessages(code, null);
	}

}
