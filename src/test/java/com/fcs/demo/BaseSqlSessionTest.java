package com.fcs.demo;

import junit.framework.TestCase;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by fengcs on 2018/4/3.
 */

public abstract class BaseSqlSessionTest extends TestCase {

    private static Logger logger = LoggerFactory.getLogger(BaseSqlSessionTest.class);

    protected SqlSession sqlSession;

    @Override
    protected void setUp() throws Exception {
        logger.debug("******************************** SqlSessionTest start up ************************************");
    }

    @Override
    protected void tearDown() throws Exception {
        logger.debug("********************************* SqlSessionTest end up *************************************");
    }

    public void testSelectWithTemplate(){
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try {
            sqlSession = sqlSessionFactory.openSession();
            doSelectTest();
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    protected abstract SqlSessionFactory getSqlSessionFactory();

    public abstract void doSelectTest();

}
