package com.ailearningvocabulary.bomnmi.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/2 18:16
 */
public class ReflectUtil {

    public static <T> Map<String, Object> parseObjectToMap(T t) throws InvocationTargetException, IllegalAccessException {
        Class<?> tClass = t.getClass();
        Map<String, Object> result = new HashMap<>();
        Method[] declaredMethods = tClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            String methodName = method.getName();
            if (methodName.contains("get")) {
                Object invoke = method.invoke(t);
                if (invoke != null) {
                    String propertyName = String.valueOf(methodName.charAt(3)).toLowerCase() + methodName.substring(4);
                    result.put(propertyName, invoke);
                }
            }
        }
        return result;
    }
}
