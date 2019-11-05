package com.project.test;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.common.LogFactory;
import com.project.test.service.TestService;

@Controller
public class TestController {

	@Autowired
	Properties configProperties;
	
	@Resource(name = "testService")
	private TestService testService;

	@RequestMapping(value = "/today", method = RequestMethod.GET)
	public ModelAndView getToday(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		ModelAndView mav = new ModelAndView("test/test");
		
		LogFactory logFactory = new LogFactory(request,configProperties);
		
		try {
			mav.addObject("getToday", testService.getToday());
		}
		catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			logFactory.logWriteError(sw.toString());
		}
		finally {
			logFactory.logWrite();
		}
		return mav;
	}
}
