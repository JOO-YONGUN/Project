package com.project.test.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.project.test.dao.TestDao;

@Service("testService")
public class TestServiceImpl implements TestService {
	

	@Resource(name = "testDao")
	private TestDao testDao;


	public String getToday() {
		return testDao.getToday();
	}
	
}
