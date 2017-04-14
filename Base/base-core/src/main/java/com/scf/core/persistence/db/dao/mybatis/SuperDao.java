package com.scf.core.persistence.db.dao.mybatis;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;

import com.scf.core.persistence.db.dao.DaoException;
import com.scf.core.persistence.db.pagination.PaginationOrdersList;

/**
 * Mybatis Base Super Dao<br/>
 * 增加异常处理功能，使用本类提供的方法，可以不用捕获操作数据库异常
 * 
 * @author wub
 */
public class SuperDao extends DaoSupport {
    @Override
    @Resource
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }
    
    /**
     * 处理mybatis操作数据库的异常<br/>
     * 支持处理一下异常
     * 
     * @param e
     */
    protected DaoException handerException(Exception e) {
        //  基于mybatis的Dao层异常处理
       
        return  DaoException.defaultDBException( e);
    }

    public <T> T selectOne(String statement) throws DaoException {
        try {
            return this.getSession().selectOne(statement);
        } catch (Exception e) {
            throw handerException(e);
        }
    }

    public <T> T selectOne(String statement, Object parameter) throws DaoException {
        try {
            return this.getSession().selectOne(statement, parameter);
        } catch (Exception e) {
            throw handerException(e);
        }
    }

    public <E> List<E> selectList(String statement) throws DaoException {
        try {
            return this.getSession().selectList(statement);
        } catch (Exception e) {
            throw handerException(e);
        }
    }

    public <E> List<E> selectList(String statement, Object parameter) throws DaoException {
        try {
            return this.getSession().selectList(statement, parameter);
        } catch (Exception e) {
            throw handerException(e);
        }
    }

    public <E> List<E> selectList(String statement, int offset, int limit) throws DaoException {
        try {
            return this.getSession().selectList(statement, null, new RowBounds(offset, limit));
        } catch (Exception e) {
            throw handerException(e);
        }
    }
    
    /**
     * 分页查询 (无参数)
     * @author wubin
     * @param statement
     * @param pol
     * @return
     * @throws DaoException
     */
    public <E> PaginationOrdersList<E> selectPage(String statement, PaginationOrdersList<E>  pol ) throws DaoException {
       return this.selectPage( statement, null,   pol);
    }
    
    
    /**
     * 分页查询（有参数）
     * @author wubin
     * @param statement
     * @param pol
     * @return
     * @throws DaoException
     */
    public <E> PaginationOrdersList<E> selectPage(String statement,Object parameter, PaginationOrdersList<E>  pol ) throws DaoException {
        try {
            //TODO 动态排序未开发
            List<E> results = this.getSession().selectList(statement, parameter, new RowBounds(pol.getPagination().getOffset(), pol.getPagination().getLimit()));
            long count = this.selectCount(statement,parameter);
            pol.getPagination().setRowCount((int)count);
            pol.setDatas(results);
            return pol;
        } catch (Exception e) {
            throw handerException(e);
        }
    }
    
    
    /**
     * 分页查询（有参数），输入参数和输出参数可以不一致
     * 返回对象o根据mapper中的resultType决定
     * @author jiangxia
     * @param statement
     * @param pol
     * @return
     * @throws DaoException
     */
    public <E, O> PaginationOrdersList<O> selectPageCustomer(String statement,Object parameter, PaginationOrdersList<E>  pol ) throws DaoException {
        try {
            //TODO 动态排序未开发
            List<O> results = this.getSession().selectList(statement, parameter, new RowBounds(pol.getPagination().getOffset(), pol.getPagination().getLimit()));
            long count = this.selectCount(statement,parameter);
            
            PaginationOrdersList<O> page = new PaginationOrdersList<O>();
            page.setPagination(pol.getPagination());
            page.getPagination().setRowCount((int)count);
            page.setDatas(results);
            return page;
        } catch (Exception e) {
            throw handerException(e);
        }
    }

    public <E> List<E> selectList(String statement, Object parameter, int offset, int limit) throws DaoException {
        try {
            return this.getSession().selectList(statement, parameter, new RowBounds(offset, limit));
        } catch (Exception e) {
            throw handerException(e);
        }
    }

    public long selectCount(String statement) throws DaoException {
        return this.selectCount(statement, null);
    }

    public long selectCount(String statement, Object parameter) throws DaoException {
        try {
            return super.selectCount(statement, parameter);
        } catch (SQLException e) {
            throw handerException(e);
        }
    }

    public int insert(String statement) throws DaoException {
        try {
            return this.getSession().insert(statement);
        } catch (Exception e) {
            throw handerException(e);
        }
    }

    public int insert(String statement, Object parameter) throws DaoException {
        try {
            return this.getSession().insert(statement, parameter);
        } catch (Exception e) {
            throw handerException(e);
        }
    }

    public int update(String statement) throws DaoException {
        try {
            return this.getSession().update(statement);
        } catch (Exception e) {
            throw handerException(e);
        }
    }

    public int update(String statement, Object parameter) throws DaoException {
        try {
            return this.getSession().update(statement, parameter);
        } catch (Exception e) {
            throw handerException(e);
        }
    }

    public int delete(String statement) throws DaoException {
        try {
            return this.getSession().delete(statement);
        } catch (Exception e) {
            throw handerException(e);
        }
    }

    public int delete(String statement, Object parameter) throws DaoException {
        try {
            return this.getSession().delete(statement, parameter);
        } catch (Exception e) {
            throw handerException(e);
        }
    }
}
