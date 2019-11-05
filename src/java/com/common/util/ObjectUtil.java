package com.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <code>ObjectUtil</code>
 * 
 * @version $Revision: 0.1 $ $Date: 0000-00-00 00:00:00 $
 * @author 
 * @history
 */
public class ObjectUtil {
	/**
	 * @param throwable
	 * @return
	 */
	public static String getStackTrace(Throwable throwable) {
		StringWriter stringwriter = new StringWriter();
		PrintWriter printwriter = new PrintWriter(stringwriter);
		throwable.printStackTrace(printwriter);
		printwriter.flush();
		return stringwriter.toString();
	}
	
	
	/**
	 * map -> json text
	 * @param throwable
	 * @return
	 */
	public static String mapToJsonStr(HashMap<?,?> map) throws Exception{
        ObjectMapper mapper = new ObjectMapper();        
        String jsonObj = mapper.writeValueAsString(map);
		
		return jsonObj;
	}
	
	/**
	 * json string -> map
	 * @param throwable
	 * @return
	 */
	public static HashMap<String, Object> jsonStrToMap(String jsonStr) throws Exception{
		ObjectMapper mapper = new ObjectMapper();  
		HashMap<String, Object> map = new HashMap<String, Object>();
		map = (HashMap<String, Object>)mapper.readValue(jsonStr, map.getClass());
		
		return map;
	}

	/**
	 * http response write ( text )
	 * @param throwable
	 * @return
	 */
    public static void responseWriteText(String responseText , HttpServletResponse response) throws Exception {
    	response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");
                
        PrintWriter pw = response.getWriter();
        try {
        	pw.print(responseText);
        }catch(Exception e){
        	
        }finally {
        	pw.flush();
    		pw.close();
        }
        return;
    }	

	/**
	 * http response write ( map�� json text �� write )
	 * @param throwable
	 * @return
	 */
    public void responseWriteJson(ConcurrentHashMap<String, Object> ctx , HttpServletResponse response) throws Exception {
    	response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");
        String jsonObj = "";
        
        ObjectMapper mapper = new ObjectMapper();        
        PrintWriter pw = response.getWriter();
        try {
        	jsonObj = mapper.writeValueAsString(ctx);
        	pw.print(jsonObj);
        }catch(Exception e){
        	
        }finally {
        	pw.flush();
    		pw.close();
        	mapper=null;
        	ctx = null;
        	jsonObj=null;
        }
        return;
    } 
    
	public static String getJsonDataBase64(String jsonData, String charset)
			throws UnsupportedEncodingException {
		return new String(Base64.encodeBase64(jsonData.getBytes(charset)));
	}
	
}
