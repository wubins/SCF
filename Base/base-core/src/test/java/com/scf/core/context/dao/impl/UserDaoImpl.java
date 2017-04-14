package com.scf.core.context.dao.impl;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scf.core.context.dao.UserDao;
import com.scf.core.context.mybatis.batch.PreparedBatch;
import com.scf.core.context.po.User;
import com.scf.core.exception.ExCode;
import com.scf.core.persistence.db.dao.DaoException;
import com.scf.core.persistence.db.dao.mybatis.SuperDao;

public class UserDaoImpl extends SuperDao implements UserDao {
	
	@Override
	public void add(User user) throws DaoException {
		try {
			this.getSession().insert("com.scf.core.context.po.User.add", user);
		} catch (Exception e) {
			throw new DaoException(ExCode.DB_001, e);
		}
	}

	@Override
	public int batchAdd(Collection<User> users) throws DaoException {
		try {
			int result = 0;
			PreparedBatch batch = this.startPreparedBatch("com.scf.core.context.po.User.add", null);
			for(User user : users) {
				batch.addBatch("com.scf.core.context.po.User.add", user);
			}
			int[] results = batch.executeBatch();
			batch.endBatch();
			
			for(int r : results) {
				result += r;
			}
			return result;
		} catch (Exception e) {
			throw new DaoException(ExCode.DB_001, e);
		}
	}

	@Override
	public void delete(int id) throws DaoException {
		try {
			this.getSession().delete("com.scf.core.context.po.User.delete", id);
		} catch (Exception e) {
			throw new DaoException(ExCode.DB_001, e);
		}
	}
	
	@Override
	public void deleteAll() throws DaoException {
		try {
			this.getSession().delete("com.scf.core.context.po.User.deleteAll");
		} catch (Exception e) {
			throw new DaoException(ExCode.DB_001, e);
		}
	}

	@Override
	public void update(User user) throws DaoException {
		try {
			this.getSession().update("com.scf.core.context.po.User.update", user);
		} catch (Exception e) {
			throw new DaoException(ExCode.DB_001, e);
		}
	}

	@Override
	public User find(int id) throws DaoException {
		try {
			User user = this.getSession().selectOne("com.scf.core.context.po.User.find", id);
			return user;
		} catch (Exception e) {
			throw new DaoException(ExCode.DB_001, e);
		}
	}
	
	@Override
	public long findAllCount() throws DaoException {
		try {
			return this.selectCount("com.scf.core.context.po.User.findAll", null);
		} catch (Exception e) {
			throw new DaoException(ExCode.DB_001, e);
		}
	}
	
	@Override
	public List<User> findAll(int offset, int limit) throws DaoException {
		try {
			List<User> users = this.getSession().selectList("com.scf.core.context.po.User.findAll", null, new RowBounds(offset, limit));
			return users;
		} catch (Exception e) {
			throw new DaoException(ExCode.DB_001, e);
		}
	}

	@Override
	public List<User> findAll() throws DaoException {
		try {
			List<User> users = this.getSession().selectList("com.scf.core.context.po.User.findAll");
			return users;
		} catch (Exception e) {
			throw new DaoException(ExCode.DB_001, e);
		}
	}
}
