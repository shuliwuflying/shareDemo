package com.flying.test;

public class MyClass {

    public static void main(String[] args){
        HelloServiceProxy proxy = new HelloServiceProxy();
        ITestInvoke iTestInvoke = new TestInvoke();
        ITestInvoke testInvoke = (ITestInvoke) proxy.bind(iTestInvoke,new Class[]{ITestInvoke.class});
        testInvoke.doSomething();
    }
}
