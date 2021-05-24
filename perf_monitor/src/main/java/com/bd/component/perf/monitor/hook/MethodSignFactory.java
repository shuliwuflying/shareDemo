package com.bd.component.perf.monitor.hook;


/**
 * 创建方法签名factory
 */
public class MethodSignFactory {

    public static MethodSign createVoidMethod(Class<?> clazz, String methodName, Class<?>[] parameterClasses) {
        return new MethodSign(clazz, methodName, parameterClasses, null);
    }

    public static MethodSign createSimpleMethod(Class<?> clazz, String methodName, Class<?> returnClass) {
        return new MethodSign(clazz, methodName, null, returnClass);
    }

    public static MethodSign createSimpleVoidMethod(Class<?> clazz, String methodName) {
        return new MethodSign(clazz, methodName, null, null);
    }

    public static MethodSign createMethod(Class<?> clazz, String methodName, Class<?>[] parameterClasses, Class<?> returnClass) {
        return new MethodSign(clazz, methodName, parameterClasses, returnClass);
    }

}
