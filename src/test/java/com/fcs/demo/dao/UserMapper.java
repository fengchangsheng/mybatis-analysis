package com.fcs.demo.dao;

import com.fcs.model.User;
import com.fcs.plugins.paging.PagingList;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by fengcs on 2018/4/3.
 */
public interface UserMapper {

    User getUserById(int id);

    PagingList<User> selectUserList(RowBounds rowBounds);

    List<User> selectUserList();

}
