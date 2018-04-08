package com.fcs.demo;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by fengcs on 2018/4/8.
 *
 * 想搞个执行模板  方便测试
 */
public class InvokeTemplate implements InvocationHandler {

    private SqlSessionFactory sqlSessionFactory;
    private Object target;

    public InvokeTemplate(SqlSessionFactory sqlSessionFactory, Object target) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
//        SqlSessionFactory sqlSessionFactory = SqlSessionHelper.getSqlSessionByXml();
        SqlSession sqlSession = null;
        Object result = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            result = method.invoke(target, args);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
        return result;
    }
}
