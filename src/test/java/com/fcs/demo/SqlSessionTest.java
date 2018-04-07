package com.fcs.demo;

import com.alibaba.druid.pool.DruidDataSource;
import com.fcs.demo.dao.UserMapper;
import com.fcs.model.User;
//import com.ibatis.common.resources.Resources;
import com.fcs.plugins.LimitInterceptor;
import com.fcs.plugins.PagingInterceptor;
import com.fcs.plugins.PagingRowBound;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.*;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by fengcs on 2018/4/3.
 */
public class SqlSessionTest {

    private static Logger logger = LoggerFactory.getLogger(SqlSessionTest.class);

    public static SqlSessionFactory getSqlSessionByCode(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/design");
        dataSource.setUsername("root");
        dataSource.setPassword("fengcs");
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("test", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
//        Interceptor interceptor = new PagingInterceptor();
        Interceptor limitInterceptor = new LimitInterceptor();
//        configuration.addInterceptor(interceptor);
        configuration.addInterceptor(limitInterceptor);
//        configuration.getTypeAliasRegistry().registerAlias();
        configuration.addMapper(UserMapper.class);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        return sqlSessionFactory;
    }

//    public static SqlSessionFactory getSqlSessionByXml(){
//        String resource = "mybatis-config.xml";
//        SqlSessionFactory sqlSessionFactory = null;
//        try {
//            InputStream inputStream = Resources.getResourceAsStream(resource);
//            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//            return sqlSessionFactory;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return sqlSessionFactory;
//    }

    public static void main(String[] args) {
        SqlSessionFactory sqlSessionFactory = getSqlSessionByCode();
//        SqlSessionFactory sqlSessionFactory = getSqlSessionByXml();
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            PagingRowBound pagingRowBound = new PagingRowBound(0, 5);
            List<User> userList = userMapper.selectUserList(pagingRowBound);
            for (User user : userList) {
                logger.info(user.getId() + " ========= " + user.getUsername());
            }
//            User user = userMapper.getUserById(1);
//            System.out.println(user.getUsername());
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

}
