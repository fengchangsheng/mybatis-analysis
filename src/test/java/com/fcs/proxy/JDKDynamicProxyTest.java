package com.fcs.proxy;

import com.fcs.model.User;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by fengcs on 2018/3/2.
 */
public class JDKDynamicProxyTest {

    private UserService userService;

    public JDKDynamicProxyTest() {
        userService = (UserService) Proxy.newProxyInstance(
                JDKDynamicProxyTest.class.getClassLoader(),
                new Class[]{UserService.class},
                new ProxyService(new UserServiceImpl()));
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public static void main(String[] args) {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        User user = new JDKDynamicProxyTest().getUserService().getUser();
        System.out.println(user.getPassword());
    }

    private static class ProxyService implements InvocationHandler{

        private Object target;

        public ProxyService(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("=======================");
            return method.invoke(target, args);
        }
    }

}
