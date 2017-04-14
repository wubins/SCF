package com.scf.core.context.dao;

import java.util.Collection;
import java.util.List;

import com.scf.core.context.po.User;
import com.scf.core.persistence.db.dao.DaoException;

public interface UserDao {
	public void add(User user) throws DaoException;
	
	// 批量增加
	public int batchAdd(Collection<User> users) throws DaoException;
	
	public void delete(int id) throws DaoException;
	public void deleteAll() throws DaoException;
	
	public void update(User user) throws DaoException;
	
	public User find(int id) throws DaoException;
	public long findAllCount() throws DaoException;
	public List<User> findAll(int offset, int limit) throws DaoException;
	public List<User> findAll() throws DaoException;
}
