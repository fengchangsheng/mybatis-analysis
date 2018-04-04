package com.fcs.demo;

import com.alibaba.druid.pool.DruidDataSource;
import com.fcs.demo.dao.UserMapper;
import com.fcs.model.User;
import com.ibatis.common.resources.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.*;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by fengcs on 2018/4/3.
 */
public class SqlSessionTest {

    public static SqlSessionFactory getSqlSessionByCode(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/design");
        dataSource.setUsername("root");
        dataSource.setPassword("fengcs");
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("test", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
//        configuration.getTypeAliasRegistry().registerAlias();
        configuration.addMapper(UserMapper.class);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        return sqlSessionFactory;
    }

    public static SqlSessionFactory getSqlSessionByXml(){
        String resource = "mybatis-config.xml";
        SqlSessionFactory sqlSessionFactory = null;
        try {
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            return sqlSessionFactory;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sqlSessionFactory;
    }

    public static void main(String[] args) {
        SqlSessionFactory sqlSessionFactory = getSqlSessionByCode();
//        SqlSessionFactory sqlSessionFactory = getSqlSessionByXml();
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = userMapper.selectUserList(new RowBounds(0,5));
            for (User user : userList) {
                System.out.println(user.getId() + "=====" + user.getUsername());
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
