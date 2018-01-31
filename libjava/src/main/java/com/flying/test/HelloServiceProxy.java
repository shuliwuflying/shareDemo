package com.flying.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by shuliwu on 2018/1/17.
 */

public class HelloServiceProxy implements InvocationHandler {

    private Object target;

    public Object bind(Object target,Class[] interfaces) {
        this.target = target;
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        System.out.println("HelloServiceProxy");
        Object result = null;
        result = method.invoke(target,objects);
        return result;
    }
}
