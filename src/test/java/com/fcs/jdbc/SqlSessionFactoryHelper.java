package com.fcs.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.fcs.demo.dao.UserMapper;
import com.fcs.util.ASCUtil;
import com.ibatis.common.resources.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by fengcs on 2018/4/8.
 */
public class SqlSessionFactoryHelper {

    /**
     * 编程式获取SqlSessionFactory
     * @param interceptor 指定的拦截器
     */
    public static SqlSessionFactory getSqlSessionFactoryByCode(Interceptor interceptor){
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        DataSource dataSource = getDataSource();
        Environment environment = new Environment("test", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        if (interceptor != null) {
            configuration.addInterceptor(interceptor);
        }
        configuration.addMapper(UserMapper.class);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        return sqlSessionFactory;
    }

    /**
     * 配置文件获取SqlSessionFactory
     */
    public static SqlSessionFactory getSqlSessionFactoryByXml(){
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

    public static DataSource getDataSource(){
        DruidDataSource dataSource = null;
        try {
            dataSource = new DruidDataSource();
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            dataSource.setUrl("jdbc:mysql://localhost:3306/design");
            dataSource.setUsername(ASCUtil.decryption("ynxQ6HGz//A="));
            dataSource.setPassword(ASCUtil.decryption("bbi7+SA4VSc="));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSource;
    }

}
