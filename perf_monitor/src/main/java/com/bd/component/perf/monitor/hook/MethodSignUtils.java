package com.bd.component.perf.monitor.hook;

import java.util.HashMap;

public class MethodSignUtils {

    private static final HashMap<Class<?>, String> sClassSignMap = new HashMap<>();

    static {
        sClassSignMap.put(void.class, "V");
        sClassSignMap.put(boolean.class, "Z");
        sClassSignMap.put(byte.class, "B");
        sClassSignMap.put(char.class, "C");
        sClassSignMap.put(short.class, "S");
        sClassSignMap.put(int.class, "I");
        sClassSignMap.put(long.class, "J");
        sClassSignMap.put(float.class, "F");
        sClassSignMap.put(double.class, "D");
    }

    protected static String buildSignString(Class<?>[] parameterClasses, Class<?> returnClass) {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        if (parameterClasses != null) {
            for (Class<?> clazz : parameterClasses) {
                String classString = getClassString(clazz);
                builder.append(classString);
            }
        }
        builder.append(")");

        String returnString = getClassString(returnClass);
        builder.append(returnString);

        return builder.toString();
    }

    protected static String buildParameterString(Class<?>[] parameterClasses) {
        StringBuilder builder = new StringBuilder();
        if (parameterClasses != null) {
            for (Class<?> clazz : parameterClasses) {
                String classString = getClassString(clazz);
                builder.append(classString);
            }
        }
        return builder.toString();
    }

    private static String getClassString(Class<?> clazz) {
        return getClassStringArray(clazz, "");
    }

    private static String getClassStringArray(Class<?> clazz, String classStrings) {
        if (clazz == null || clazz == void.class) {
            return sClassSignMap.get(null);
        }

        if (clazz.isArray()) {
            return getClassStringArray(clazz.getComponentType(), classStrings + "[");
        }
        String classString = sClassSignMap.get(clazz);
        if (classString == null) {
            String className = clazz.getName();
            className = className.replace(".", "/");
            classStrings += "L" + className + ";";
        } else {
            classStrings += classString;
        }
        return classStrings;
    }
}
