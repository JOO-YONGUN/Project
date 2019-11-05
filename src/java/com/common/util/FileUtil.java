package com.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URLEncoder;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.io.Resources;
import org.springframework.util.FileCopyUtils;

/**  
 * @Class Name : FileUtil.java
 * @Description : FileUtil Class
 * @Modification Information  
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2019.05.15  주용운         최초생성
 *
 * @author 클라우드 모바일 약관발송 시스템 
 * @since 2019.04.26
 * @version 1.0
 * @see
 *
 *  Copyright (C) by 2019 KICO All right reserved.
 */
public class FileUtil {
	
	/**
	 * source 파일을 target 파일로 복사한다.
	 * 
	 * @param source Source 파일
	 * @param target Target 파일
	 * @return 복사 성공 여부
	 * @throws IOException
	 */
	public static boolean copyFile(String source, String target) {
		boolean result = false;
		
		//if(log.isDebugEnabled())
		//	log.debug(String.format("FileUtil.copyFile %s -> %s", source, target));
		
		String strSource = StringUtil.getPath(source);
		File f1 = new File(strSource);
		
		String strTarget = StringUtil.getPath(target);
		File f2 = new File(strTarget);
		
		InputStream in = null;
		//For Overwrite the file.
		OutputStream out = null;
		
		try{
			f2.createNewFile();
			
			in = new FileInputStream(f1);
			out = new FileOutputStream(f2);
			byte[] buf = new byte[1024];
			int len = in.read(buf);
			
			while (len > 0){
				out.write(buf, 0, len);
				len = in.read(buf);
			}
			
			result = true;
		} catch(Exception e) {
		} finally {
			if(in!= null) {
				try{
					in.close();
				} catch(Exception e) {
				}
			}
			if(out!= null) {
				try{
					out.close();
				} catch(Exception e) {
				}
			}
		}
		
		return result;
	}
	
	public static boolean isExist(String fullPath){
		//if(log.isDebugEnabled())
		//	log.debug(String.format("isExist fullPath : %s", fullPath));
		
		String strFullPath = StringUtil.getPath(fullPath);
		File f = new File(strFullPath);
		return f.exists();
	}
	

	/**
	 * 파일삭제
	 * 
	 * @param fullPath 파일 경로
	 * @return 삭제 여부
	 */
	public static boolean deleteFile(String fullPath){
		String strFullPath = StringUtil.getPath(fullPath);
		File f = new File(strFullPath);
		
		return f.delete();
	}
	
	/**
	 * 파일 크기 구하기
	 *
	 * @param fullPath File path
	 * @return 파일 크기
	 */
	public static long getLength(String fullPath){
		long rtn = 0;
		
		String strFullPath = StringUtil.getPath(fullPath);
		File f = new File(strFullPath);
		if(!f.exists())
			return rtn;
		
		if(f.isFile())
			rtn = f.length();
		
		return rtn;
	}
	
	/**
	 * 파일 확장자 구하기
	 * 
	 * @param fileName 파일명
	 * @return 확장자
	 */
	public static String getFileExt(String fileName) {
		int lastIndexOf = fileName.lastIndexOf('.');
		String ext = "";
		
		if (lastIndexOf > -1) {
			ext = fileName.substring(lastIndexOf + 1);
		}
		
		return ext;
	}
	
	/**
	 * 파일 내용을 읽어 String으로 반환
	 * 
	 * @param filePath 파일 경로
	 * @return 파일 내용
	 */
	public static String getFileContent(String filePath) {
		return getFileContent(filePath,  System.getProperty("file.encoding"));
	}

	
	
	/**
	 * 파일 내용을 읽어 String으로 반환
	 * 
	 * @param filePath 파일 경로
	 * @param encoding 읽을때 Encoding Type 지정
	 * 
	 * @return 파일 내용
	 */
	public static String getFileContent(String filePath, String encoding) {
		StringBuffer content = new StringBuffer();
		
        BufferedReader br = null;
        String line = "";
        
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), encoding));
			
			while((line = br.readLine()) != null) {
				content.append(line).append("\n");
			}
		} catch(Exception e) {
		} finally {
			try {
                if (br != null) br.close();
            } catch (Exception e) {
            }
		}
		
		return content.toString();
	}
	
	/**
	 * String 전체를 파일에 기록
	 * 
	 * @param filePath 파일 경로
	 * @param content 파일 내용
	 * @return 파일 내용
	 */
	public static void putFileContent(String filePath, String content) throws IOException {
		FileWriter fw = null;
		
		try {
			fw = new FileWriter(filePath);
			fw.write(content);
		} finally {
			try {
				if(fw!=null) fw.close();
			} catch (IOException e) {
			}
		}
	}
	
	/**
	 * 파일을 다운로드한다.
	 * 
	 * @param pFileName 사용자에게 보여지는 파일명
	 * @param pFilePath /[시스템코드]/[서버에 저장된 파일명]
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws Exception
	 */
	public static void downloadFile(String pFileName, String pFilePath,
			HttpServletRequest request,	HttpServletResponse response) throws Exception {
		downloadFile(pFileName, pFilePath, request, response, false);
	}
	
	/**
	 * 파일을 다운로드하고 삭제 플래그가 true인 경우 파일을 삭제한다.
	 * 
	 * @param pFileName 사용자에게 보여지는 파일명
	 * @param pFilePath /[시스템코드]/[서버에 저장된 파일명]
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param delete 삭제 여부
	 * @throws Exception
	 */
	public static void downloadFile(String pFileName, String pFilePath,
			HttpServletRequest request,	HttpServletResponse response, boolean delete) throws Exception {

		String rootPath = System.getProperty("uploadpath");
		downloadFile(pFileName, rootPath, pFilePath, request, response, delete);
	}
	
	
	/**
	 * 파일 다운로드 시 파일저장 RootPath를 Parameter로 입력받는다.
	 * 
	 * @param pFileName 사용자에게 보여지는 파일명
	 * @param rootPath 파일저장 RootPath
	 * @param pFilePath /[시스템코드]/[서버에 저장된 파일명]
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param delete 삭제 여부
	 * @throws Exception
	 */
	public static void downloadFile(String pFileName, String rootPath, String pFilePath,
			HttpServletRequest request,	HttpServletResponse response, boolean delete) throws Exception {
		// 웹보안 처리
		String referer = request.getHeader("Referer");
		
		if(referer==null || "".equals(referer)) {
			response.sendRedirect("/jsp/common/errorUserSessionRequired.jsp");
			return;
		}
		
		// String serverName = xmlConfiguration.getString("server.name");
		
		String fileName = pFileName;
		String filePath = pFilePath;
		
		/*
		if("local".equals(serverName)) {
			fileName = new String(pFileName.getBytes("ISO-8859-1"), "MS949");
			filePath = new String(pFilePath.getBytes("ISO-8859-1"), "MS949");
		}
		*/
		
		if(!filePath.startsWith("/")) {
			filePath = "/" + filePath;
		}
		
		// 해킹방지 : systemCd에 대한 경로순회 문자열 필터링
		if(filePath.indexOf("..")!=-1 || filePath.indexOf("&")!=-1) {
			String message = "경로순회에 의한 해킹 시도가 발생하였습니다.";
			printError(response, message+"\\n모니터링되고 있습니다. 주의하십시오.");
		} else {
			File uFile = new File(rootPath + filePath);
			int fSize = (int) uFile.length();
			
			if (fSize > 0) {
				BufferedInputStream bis = null;
				ServletOutputStream sos = null;
				
				try {
					bis = new BufferedInputStream(new FileInputStream(uFile));
					sos = response.getOutputStream();
					
					response.setContentType("application/octet-stream; charset=ISO-8859-1");
					response.setHeader("Content-Disposition", "attachment; filename=\""	+ URLEncoder.encode(fileName, "UTF-8") + "\"");
					response.setHeader("Content-Transfer-Encoding", "binary");
					response.setContentLength(fSize);
					
					FileCopyUtils.copy(bis, sos);
					sos.flush();
				} finally {
					if(bis!=null) bis.close();
					if(sos!=null) sos.close();
					
					// 다운로드 후 삭제
					if(delete) {
						uFile.delete();
					}
				}
			} else {
				printError(response, "존재하지 않는 파일입니다.");
			}
		}
	}
	
	private static void printError(HttpServletResponse response, String message) throws IOException {
		PrintWriter pw = null;
		
		try {
			// setContentType을 프로젝트 환경에 맞추어 변경
			response.setContentType("text/html; charset=EUC-KR");
			
			pw = response.getWriter();
			pw.println("<html><head>");
			pw.println("<script>alert(\"" + message + "\");</script>");
			pw.println("</head><body></body></html>");
			pw.flush();
		} finally {
			if(pw!=null) pw.close();
		}
	}
	
	/*
	public static void main(String[] args) throws Exception { 
		String str = getFileContent("/mail_template/freeMedicalMail.html");
		System.out.println(str);
	}
	*/
	
    public static String getSaveLocation() {

    	String resource = "/config.properties";
    	Properties properties = new Properties();
    	String returnPath = "";
    	
    	try {
    		Reader reader = Resources.getResourceAsReader(resource);
    		properties.load(reader);
    		
    		returnPath = properties.getProperty("kimss.server.upload.path");
    	}
    	catch (Exception e) {
    		returnPath = "";
    	}
    	
    	return returnPath;
    }
}
