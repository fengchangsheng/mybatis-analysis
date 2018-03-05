package com.fcs.proxy;

import com.fcs.model.User;

/**
 * Created by fengcs on 2018/3/2.
 */
public class UserServiceImpl implements UserService {
    @Override
    public User getUser() {
        User user = new User();
        user.setUsername("lucare");
        user.setPassword("123456");
        return user;
    }
}
