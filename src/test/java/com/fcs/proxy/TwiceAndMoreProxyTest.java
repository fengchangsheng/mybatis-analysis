package com.fcs.proxy;

import com.fcs.demo.service.UserService;
import com.fcs.demo.service.impl.UserServiceImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by fengcs on 2018/4/10.
 */
public class TwiceAndMoreProxyTest {

    public static Object wrap(Object target){
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new ProxyService(target));
    }

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        Object object = wrap(userService);


    }

    private static class ProxyService implements InvocationHandler {

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
