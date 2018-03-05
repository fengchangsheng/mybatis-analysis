package com.fcs.model;

import com.fcs.model.ExtUserInfo;

/**
 * Created by fengcs on 2018/3/2.
 */
public class User {

    private String username;
    private String password;
    private int age;

    private ExtUserInfo extUserInfo;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ExtUserInfo getExtUserInfo() {
        return extUserInfo;
    }

    public void setExtUserInfo(ExtUserInfo extUserInfo) {
        this.extUserInfo = extUserInfo;
    }
}
