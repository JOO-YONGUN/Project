package com.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <code>StringUtil</code>
 * 
 * @version $Revision: 0.1 $ $Date: 0000-00-00 00:00:00 $
 * @author 
 * @history
 */
public class StringUtil {
    private final static String EMPTY_STRING = "";
    private final static String HTML_TAG_PATTERN = "(.+)? ";
    
    /**
     * null 또는 공백 문자열인 경우 치환 문자열로 변환하고 그렇지 않은 경우에는 trim하여 반환한다.
     * 
     * @param origin 원본 문자열
     * @param replaceString 치환 문자열
     * @return 변환된 문자열
     */
    public static String nvl(String origin, String replaceString) {
        return (origin == null || EMPTY_STRING.equals(origin)) ? replaceString : origin.trim();
    }
    
    /**
     * Object를 문자열로 변환한 후, null 또는 공백 문자열인 경우 치환 문자열로 변환하고 그렇지 않은 경우에는 trim하여 반환한다.
     * 
     * @param origin 원본 Object
     * @param replaceString 치환 문자열
     * @return 변환된 문자열
     */
    public static String nvl(Object origin, String replaceString) {
        return (origin == null || EMPTY_STRING.equals(origin.toString())) ? replaceString : origin.toString().trim();
    }
    
    /**
     * HTML 시작 태그(최초 공백전까지의 모든 문자열)을 리턴한다.
     * 
     * @param str 원본 문자열
     * @return HTML 시작 태그
     */
    public static String getHtmlBeginTag(final String str) {
        if (str == null || EMPTY_STRING.equals(str))
            return null;

        Matcher m = Pattern.compile(HTML_TAG_PATTERN).matcher(str);
        if (m.find()) {
            return m.group(0);
        }
        return EMPTY_STRING;
    }
    
    /**
     * HTML 닫힘 태그를 가져온다.
     * 
     * @param str 원본 문자열
     * @return HTML 닫힘 태그
     */
    public static String getHtmlEndTag(String str) {
        String beginTag = StringUtil.nvl(getHtmlBeginTag(str), EMPTY_STRING);
        
        if(!EMPTY_STRING.equals(beginTag)) {
            return "</" + beginTag.substring(1).trim() + ">"; 
        }
        return EMPTY_STRING;
    }
    
    /**
     * 문자열 중 치환 대상 문자열을 치환 문자열을 변환한다.
     * 
     * @param s 원본 문자열
     * @param old 치환 대상 문자열
     * @param replacement 치환 문자열
     * @return 변환된 문자열
     */
    public static String replace(String s, String old, String replacement) {
        if (s == null || old == null)
            return EMPTY_STRING;
        int i = s.indexOf(old);
        StringBuffer r = new StringBuffer();

        if (i == -1)
            return s;
        r.append(s.substring(0, i)).append(replacement);
        if (i + old.length() < s.length())
            r.append(replace(s.substring(i + old.length(), s.length()), old, replacement));

        return r.toString();
    }

    /**
     * 파일 경로에서 상위 경로 문자열을 제거한다.
     * @param s 파일 경로
     * @return 상위 경로 문자열이 제거된 파일 경로
     */
    public static String getPath(String s) {
        String r;
        if (s == null) return EMPTY_STRING;
        
        r = StringUtil.replace(s, "..", "");
        
        return r;
    }

    /**
     * 캐리지리턴을 BR로 바꾼다.
     * 
     * @param xTo 원본 문자열
     * @return 변환된 문자열
     */
    /*
    public static String convertFromCRLFToBR(String xTo) {
    	String x = xTo;
        StringBuffer sb = null;
        StringReader sr = null;
        BufferedReader br = null;
        
        try {
            sr = new StringReader(x.concat("\n"));
            br = new BufferedReader(sr);
            String s = br.readLine();
            sb = new StringBuffer();

            while (s != null) {
                sb.append(s);
                sb.append("<br/>");
                s = br.readLine();
            }
        } catch(Exception e) {
        	throw new ExRuntimeException(e);
        } finally {
            try {
            	if(br!=null) br.close();
			} catch (IOException e) {
				throw new ExRuntimeException(e);
			}
            sr.close();
        }
        return sb.toString();
    }
    */
    
    /**
     * JavaScript를 Disable시킨다.
     * 
     * @param param 원본 문자열
     * @return JavaScript가 Disable된 문자열
     */
    /*
    public static String disableScript(String param) {
    	String result = param;
    	
    	ExXMLConfiguration conf = ExXMLConfiguration.getInstance();
    	
    	String[] tagNames = conf.getStringArray("security.web-security.xss.disbled-tag"); // frame은 frameset 포함
    	String[] eventNames = conf.getStringArray("security.web-security.xss.disbled-event");
    	
    	for(int i=0; i<tagNames.length; i++) {
    		result = disableScriptTag(result, tagNames[i]);
    	}
    	
    	for(int i=0; i<eventNames.length; i++) {
    		result = disableScriptEvent(result, eventNames[i]);
    	}
		
		return result;
	}
    */
    
    private static String disableScriptTag(String param, String tagName) {
    	String result = param;
		int inx = 0;
		
		while(true) {
			inx = result.toLowerCase().indexOf("<"+tagName, inx);
			
			if(inx==-1) break;
			
			result = replaceToLt(result, inx);
			
			inx = result.toLowerCase().indexOf(">", inx); // ...> or .../>
			
			result = replaceToGt(result, inx);
		}
		
		inx = 0;
		
		while(true) {
			inx = result.toLowerCase().indexOf("</"+tagName, inx);
			
			if(inx==-1) break;
			
			result = replaceToLt(result, inx);
			
			inx = result.toLowerCase().indexOf(">", inx); // ...> or .../>
			
			result = replaceToGt(result, inx);
		}
		
		return result;
	}
    
    private static String replaceToLt(String param, int inx) {
		param = param.substring(0, inx) + "&lt;" + param.substring(inx+1);
		
		return param;
	}
	
	private static String replaceToGt(String param, int inx) {
		param = param.substring(0, inx) + "&gt;" + param.substring(inx+1);
		
		return param;
	}
	
	private static String disableScriptEvent(String param, String eventName) {
		String result = param;
		int inx = 0;
		
		while(true) {
			inx = result.toLowerCase().indexOf(eventName, inx);
			
			if(inx==-1) break;
			
			result = result.substring(0, inx) + "_" + eventName + result.substring(inx+eventName.length());
			
			inx += inx+eventName.length();
		}
		
		return result;
	}
	
	/**
	 * 정해진 길이 만큼 숫자와 알파벳을 혼합하여 랜덤 문자열을 생성하여 반환
	 * 
	 * @param length 랜덤 문자열 길이
	 * @return 랜덤 문자열
	 */
	public static String getRandomString(int length) {
		StringBuffer rndResult = new StringBuffer();
		String numberAndAlpha = "123456789012345678901234567890ABCDEFGHJKLMNPQRSTUVWXYZ"; // 혼란을 방지하기 위해 I, O 제외
		int rndIndex = 0;
		
		Random rnd = new Random();
		
		for(int i=0; i<length; i++) {
			rndIndex = rnd.nextInt(numberAndAlpha.length());
			
			rndResult.append(numberAndAlpha.charAt(rndIndex));
		}
		
		return rndResult.toString();
	}
	
    /**
     * 자릿수 맞추기 
     * 예) "3" --> "003" : fixCipher(3, "3");
     * 
	 * @param i 전체 길이
	 * @param s 문자열
	 * @return 변환된 문자열
	 */
	public static String fixCipher(int i, String s)
    {
        StringBuffer stringbuffer = new StringBuffer();
        int j = s.length();
        if(j < i)
        {
            for(int k = 0; k < i - j; k++)
                stringbuffer.append("0");

            stringbuffer.append(s);
            return stringbuffer.toString();
        } else
        {
            return s;
        }
    }

	/**
	 * 주민번호로 19세 이하인지 체크 
	 * 
	 * @param juminnum 주민번호
	 * @return 19세 이하 : true , 20세 이상 : false
	 */
	public static boolean isUnder19Age(String juminnum) {
	    boolean chk = false;

	    java.util.Date Today = new java.util.Date();

	    String Birth = juminnum.substring(0,6);
	    String Gubun = juminnum.substring(6,7);
	    String Byear = "";
	    int Age = 0;
	    String NowDate = null;

	    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	    String strToday = format.format(Today);

	    // 1900년도와 2000년도 구분
	    if(Integer.parseInt(Gubun) < 3)
	    {
	        Byear = "19" + Birth.substring(0,2);
	    }else{
	        Byear = "20" + Birth.substring(0,2);
	    }

	    //만나이 구하기
	    Age = Integer.parseInt(strToday.substring(0,4)) - Integer.parseInt(Byear);

	    //이번년 생년월일
	    NowDate = Integer.toString(Integer.parseInt(Byear) + Age) + juminnum.substring(2,6);

	    //System.out.println("NowDate :"+NowDate);

	    if((Integer.parseInt(NowDate) - Integer.parseInt(strToday))<0) {
	        Age = Age -1;
	    }

	    //System.out.println("Age :"+Age);

	    // 19세 이하 : true , 20세 이상 : false
	    if (Age <= 19) {
	        chk = true;
	    }

	    return chk;
	}
	
	/*
	public static void main(String[] args) {
		System.out.println(getRandomString(8));
	}
	*/
	
	/**
	 * 날짜(8자) 유효성 체크
	 * 
	 * @param chkDate 날짜
	 * @return boolean
	 */
	public static boolean isValidationDate (String chkDate) {
		
		boolean flag = false; 
		
		try {
			int length = chkDate.length();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmdd"); 
			
			if (length == 8) {
				simpleDateFormat.setLenient(false);
				simpleDateFormat.parse(chkDate);
				flag = true;
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 휴대폰 번호 유효성 체크
	 * 
	 * @param chkPhone 휴대폰 번호
	 * @return boolean
	 */
	public static boolean isCellPhone (String chkPhone) {
		
		return chkPhone.matches("(01[016789](\\d{3,4})(\\d{4}))");
	}
	
	/**
	 * 월 조회
	 * 
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<String> getMonthInti() throws Exception {

		ArrayList<String> listDay = new ArrayList<String>();
		
		for (int i = 1; i <= 12; i++) {
			listDay.add( i <= 9 ? "0" + String.valueOf(i) : String.valueOf(i) );
		}
		
		return listDay;
	}
	
	/**
	 * 일자 조회
	 * 
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<String> getDayInti() throws Exception {

		ArrayList<String> listDay = new ArrayList<String>();
		
		for (int i = 1; i <= 31; i++) {
			listDay.add( i <= 9 ? "0" + String.valueOf(i) : String.valueOf(i) );
		}
		
		return listDay;
	}
	
	/**
	 * 숫자형태 체크
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumberic(String str) {
		try {
			Double.parseDouble(str);
			return true;
		}
		catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Date 형태 체크
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDate(String str) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			df.setLenient(false);
			df.parse(str);
			
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 포함여부 체크
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isContains(String key, String value, String gbu) {
		StringTokenizer stringTokenizer = new StringTokenizer(key, gbu);
		int cnt = 0;
		while (stringTokenizer.hasMoreTokens()) {
			if (stringTokenizer.nextToken().equals(value)) {
				cnt ++;
			}
		}
		return (0 < cnt ? true : false);
	}
	
}
