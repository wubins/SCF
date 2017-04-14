package com.scf.core.context.mybatis.batch;

import java.sql.SQLException;
import java.sql.Statement;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionUtils;

import com.scf.core.persistence.db.util.JdbcUtils;
/**
 * 抽象batch包装类
 * @author wubin
 * @date 2016年11月25日 下午9:04:46 
 * @version V1.1.0
 */
public abstract class AbstractBatch {
    
	protected SqlSession sqlSession;
	
	protected Statement statement;
	
	protected SqlSessionFactory sqlSessionFactory;
	
	/**
	 * 通过map映射的id和参数增加batch
	 * 
	 * @param statement
	 * @param parameter
	 * @throws SQLException
	 */
	public abstract void addBatch(String statement, Object parameter) throws SQLException;
	
	/**
	 * statement executeBatch
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int[] executeBatch() throws SQLException {
		return statement.executeBatch();
	}
	
	/**
	 * close statement and clear cache
	 * @throws SQLException
	 */
	public void endBatch() throws SQLException {
		JdbcUtils.closeStatement(statement);
		//this.sqlSession.clearCache();

        JdbcUtils.closeStatement(statement);
        if (null != sqlSession && null != sqlSessionFactory) {
            SqlSessionUtils.closeSqlSession(sqlSession, sqlSessionFactory);
        }
    
		
	}

	/**
	 * get Mybatis SqlSession
	 * @return
	 */
	public SqlSession getSqlSession() {
		return sqlSession;
	}
}
