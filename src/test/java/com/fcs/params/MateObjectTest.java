package com.fcs.params;

import com.fcs.model.ExtUserInfo;
import com.fcs.model.User;
import org.apache.ibatis.reflection.MetaObject;

/**
 * Created by fengcs on 2018/3/2.
 */
public class MateObjectTest {

    public static void main(String[] args) {
        User user = new User();
        ExtUserInfo extUserInfo = new ExtUserInfo();
        extUserInfo.setAddress("深圳市南山区");
        user.setAge(25);
        user.setUsername("fcs");
        user.setExtUserInfo(extUserInfo);
        MetaObject metaObject = MetaObjectHelper.newMetaObject(user);
        System.out.println(metaObject.getValue("username"));
        System.out.println(metaObject.getValue("extUserInfo.address"));

    }
}
