package com.scf.core.context.mybatis.batch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.logging.jdbc.ConnectionLogger;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.SqlSessionUtils;

import com.scf.core.context.mybatis.MybatisUtils;

/**
 * PreparedBatch的Batch包装类
 * @author wubin
 * @date 2016年11月25日 下午9:04:46 
 * @version V1.1.0
 */
public class PreparedBatch extends AbstractBatch {
	/**
	 * 根据Map配置生成响应的PreparedStatement<br/>
	 * 参考org.apache.ibatis.executor.statement.PreparedStatementHandler#instantiateStatemen
	 * 
	 * @param sqlSession
	 * @param statement
	 * @param parameter
	 * @throws SQLException
	 */
	public PreparedBatch(SqlSession sqlSession, String statement, Object parameter) throws SQLException {
		this.sqlSession = sqlSession;
		
		// org.apache.ibatis.executor.statement.PreparedStatementHandler#instantiateStatement
	
		SqlSessionFactory sf = null;
		SqlSession ss = null;
		SqlSessionTemplate st = (SqlSessionTemplate) sqlSession;
		sf = st.getSqlSessionFactory();
		this.sqlSessionFactory = sf;
        ss = SqlSessionUtils.getSqlSession(sf, st.getExecutorType(), st.getPersistenceExceptionTranslator());
        Connection connection = ss.getConnection();
        // Connection connection = this.sqlSession.getConnection(); bug get closed conn!
		
		if (MybatisUtils.PREPAREDSTATEMENT_LOG.isDebugEnabled() || MybatisUtils.CONNECTION_LOG.isDebugEnabled()) { // log proxy
			connection =  ConnectionLogger.newInstance(connection, MybatisUtils.PREPAREDSTATEMENT_LOG);
		}
		MappedStatement mappedStatement = this.sqlSession.getConfiguration().getMappedStatement(statement);
		String sql = mappedStatement.getBoundSql(parameter).getSql(); // PreparedStatement sql
	    if (mappedStatement.getKeyGenerator() instanceof Jdbc3KeyGenerator) {
	      String[] keyColumnNames = mappedStatement.getKeyColumns();
	      if (keyColumnNames == null) {
	    	  this.statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
	      } else {
	    	  this.statement = connection.prepareStatement(sql, keyColumnNames);
	      }
	    } else if (mappedStatement.getResultSetType() != null) {
	    	this.statement = connection.prepareStatement(sql, mappedStatement.getResultSetType().getValue(), ResultSet.CONCUR_READ_ONLY);
	    } else {
	    	this.statement = connection.prepareStatement(sql);
	    }
	    // org.apache.ibatis.executor.statement.PreparedStatementHandler#instantiateStatement
	}
	
	@Override
	public void addBatch(String statement, Object parameter) throws SQLException {
		MappedStatement mst = this.sqlSession.getConfiguration().getMappedStatement(statement);
		BoundSql boundSql = mst.getBoundSql(parameter); // PreparedStatement sql
		
		MybatisUtils.setParameters((PreparedStatement) this.statement, mst, boundSql, parameter); // set parameters
		((PreparedStatement)this.statement).addBatch();
	}
	
	/**
	 * get PreparedStatement
	 * @return
	 */
	public PreparedStatement getStatement() {
		return (PreparedStatement)statement;
	}
}
