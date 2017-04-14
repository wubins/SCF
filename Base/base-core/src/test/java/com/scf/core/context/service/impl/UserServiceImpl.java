package com.scf.core.context.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scf.core.context.dao.UserDao;
import com.scf.core.context.po.User;
import com.scf.core.context.service.BmoException;
import com.scf.core.context.service.UserService;
import com.scf.core.persistence.db.dao.DaoException;


public class UserServiceImpl implements UserService {
	
	@Resource(name = "userDaoImpl")
	private UserDao userDaoImpl;
	
	@Override
	public long count() throws BmoException {
		try {
			return this.userDaoImpl.findAllCount();
		} catch (DaoException e) {
			throw new BmoException(e);
		}
	}
	
	@Override
	@Transactional
	public void deleteAll() throws BmoException {
		try {
			this.userDaoImpl.deleteAll();
		} catch (DaoException e) {
			throw new BmoException(e);
		}
	}

	@Override
	@Transactional
	public void testTransationCommit() throws BmoException {
		try {
			User user = new User("xeigang", "123456", "");
			this.userDaoImpl.add(user);
			
			List<User> users = new ArrayList<User>();
			for(int i = 1; i <= 10; i++) {
				users.add(new User("xiegang" + i, "p" + i, ""));
			}
			this.userDaoImpl.batchAdd(users);
		} catch (DaoException e) {
			throw new BmoException(e);
		}
	}

	@Override
	@Transactional
	public void testTransationRollback() throws BmoException {
		this.testTransationCommit();
		
		throw new RuntimeException();
	}

	public void setUserDaoImpl(UserDao userDaoImpl) {
		this.userDaoImpl = userDaoImpl;
	}
}
