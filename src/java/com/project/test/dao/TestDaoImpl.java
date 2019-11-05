package com.project.test.dao;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("testDao")
public  class TestDaoImpl implements TestDao {
	
	@Autowired
	@Resource(name = "sqlSession")
	private SqlSession sqlSession;
	
	public String getToday() {
		return sqlSession.selectOne("test.getToday");
	}
	
}


