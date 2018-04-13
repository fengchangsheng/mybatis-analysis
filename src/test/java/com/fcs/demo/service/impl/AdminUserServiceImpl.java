package com.fcs.demo.service.impl;

import com.fcs.demo.service.UserService;
import com.fcs.model.User;

/**
 * Created by fengcs on 2018/4/10.
 */
public class AdminUserServiceImpl implements UserService {
    @Override
    public User getUser() {
        User user = new User();
        user.setUsername("admin");
        user.setAge(50);
        return user;
    }

    @Override
    public Object plugin() {
        return null;
    }
}
