package com.bd.component.perf.monitor.hook;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 方法签名
 */
public class MethodSign {
    private final Class<?> clazz;
    private final String methodName;
    private final Class<?>[] signParameterClasses;
    private final Class<?> returnClass;


    MethodSign(Class<?> clazz, String methodName, Class<?>[] signParameterClasses, Class<?> returnClass) {
        this.clazz = clazz;
        this.methodName = methodName;
        this.signParameterClasses = signParameterClasses;
        this.returnClass = returnClass;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getSignParameterClasses() {
        return signParameterClasses;
    }

    public Class<?> getReturnClass() {
        return returnClass;
    }

    public Method getMethod() {
        try {
            return clazz.getDeclaredMethod(methodName, signParameterClasses);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "MethodSign{" +
                "clazz=" + clazz +
                ", methodName='" + methodName + '\'' +
                ", signParameterClasses=" + Arrays.toString(signParameterClasses) +
                ", returnClass=" + returnClass +
                '}';
    }
}
