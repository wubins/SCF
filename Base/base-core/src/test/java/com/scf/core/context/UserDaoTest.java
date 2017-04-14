package com.scf.core.context;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.scf.core.context.dao.UserDao;
import com.scf.core.context.po.User;

import junit.framework.Assert;

@ContextConfiguration(locations = { "classpath*:scf/spring-ctx.xml",  "classpath*:spring-ctx-ds.xml", "classpath*:spring-ctx-tx.xml"})
public class UserDaoTest extends AbstractJUnit4SpringContextTests {
	@Resource
	private UserDao userDaoImpl;
	
	private void clearData() {
		userDaoImpl.deleteAll();
	}
	
	@Before
	public void setUp() throws Exception {
		clearData();
	}
	
//	@After
//	 @Ignore
//	public void tearDown() throws Exception {
//		clearData();
//	}
	
	@Test
	public void testFindAll() {
		int insertSize = 10;
		for(int i = 1; i <= insertSize; i++) {
			userDaoImpl.add(new User("wubin" + i, "p" + i, ""));
		}
		List<User> users = userDaoImpl.findAll();
		Assert.assertEquals(insertSize, users.size());
	}
	
	@Test
	public void testPagination() {
		int insertSize = 10;
		for(int i = 1; i <= insertSize; i++) {
			userDaoImpl.add(new User("wubin" + i, "p" + i, ""));
		}
		Assert.assertEquals(insertSize, this.userDaoImpl.findAllCount());
		
		List<User> users = this.userDaoImpl.findAll(2, 3);
		Assert.assertEquals("wubin3", users.get(0).getUsername());
	}
	
	@Test
	public void testBatch() {
		int insertSize = 10;
		List<User> users = new ArrayList<User>();
		for(int i = 1; i <= insertSize; i++) {
			users.add(new User("wubin" + i, "p" + i, ""));
		}
		this.userDaoImpl.batchAdd(users);
		
		List<User> users1 = userDaoImpl.findAll();
		Assert.assertEquals(insertSize, users1.size());
	}

	public void setUserDaoImpl(UserDao userDaoImpl) {
		this.userDaoImpl = userDaoImpl;
	}
}
