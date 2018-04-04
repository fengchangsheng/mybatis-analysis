package com.fcs.demo.dao;

import com.fcs.model.User;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by fengcs on 2018/4/3.
 */
public interface UserMapper {

    User getUserById(int id);

    List<User> selectUserList(RowBounds rowBounds);

}
