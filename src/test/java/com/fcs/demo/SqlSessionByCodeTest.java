package com.fcs.demo;

import com.fcs.demo.dao.UserMapper;
import com.fcs.jdbc.SqlSessionFactoryHelper;
import com.fcs.model.User;
import com.fcs.plugins.paging.Paging;
import com.fcs.plugins.paging.PagingInterceptor;
import com.fcs.plugins.paging.PagingList;
import com.fcs.plugins.paging.PagingRowBound;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by fengcs on 2018/4/8.
 */
public class SqlSessionByCodeTest extends BaseSqlSessionTest {

    private static Logger logger = LoggerFactory.getLogger(SqlSessionByCodeTest.class);

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected SqlSessionFactory getSqlSessionFactory() {
        Interceptor interceptor = new PagingInterceptor();
//        interceptor = new LimitInterceptor();
        return SqlSessionFactoryHelper.getSqlSessionFactoryByCode(interceptor);
    }

    public void testWithPaging(){
        testSelectWithTemplate();
    }

    @Override
    public void doSelectTest() {
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        // 分页验证
        PagingRowBound pagingRowBound = new PagingRowBound(0, 5);
        PagingList<User> userList = userMapper.selectUserList(pagingRowBound);
        printUsers(userList);
        Paging paging = userList.getPaging();
        logger.info("paging > totalCount: {}, totalPage: {}" , paging.getTotalCount() , paging.getToatalPage());
        // 缓存验证
        List<User> sameUserList = userMapper.selectUserList(pagingRowBound);
        // 无分页验证
        List<User> allList = userMapper.selectUserList();
        printUsers(allList);
        // 单个对象验证
        User user = userMapper.getUserById(1);
        logger.info("username is {}", user.getUsername());
    }

    private void printUsers(List<User> userList){
        if (userList != null && !userList.isEmpty()) {
            for (User user : userList) {
                logger.info(user.getId() + " : " + user.getUsername());
            }
        }
    }
}
