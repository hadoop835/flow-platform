package com.flow.platform.util;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by gy@fir.im on 26/06/2017.
 * Copyright fir.im
 */
public class ObjectUtil {

    /**
     * Convert field name xxYyZz to xx_yy_zz
     *
     * @return flatted field name
     */
    public static String convertFieldNameToFlat(String fieldName) {
        StringBuilder builder = new StringBuilder(fieldName.length() + 10);
        for (int i = 0; i < fieldName.length(); i++) {
            char c = fieldName.charAt(i);
            if (Character.isUpperCase(c)) {
                builder.append("_").append(Character.toLowerCase(c));
                continue;
            }
            builder.append(c);
        }
        return builder.toString();
    }

    public static boolean assignValueToField(Field field, Object bean, Object value) {
        Class<?> aClass = bean.getClass();
        try {
            String setterMethodName = "set" + fieldNameForSetterGetter(field.getName());
            Method method = aClass.getDeclaredMethod(setterMethodName, field.getType());
            method.invoke(bean, convertType(field, value));
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    private static Object convertType(Field field, Object value) {
        if (field.getType().equals(value.getClass())) {
            return value;
        }

        if (field.getType() == Integer.class) {
            return Integer.parseInt(value.toString());
        }

        throw new IllegalArgumentException(
                String.format("Type from '%s' to '%s' not supported yet", value.getClass(), field.getType()));
    }

    public static Field[] getFields(Class<?> clazz) {
        return clazz.getDeclaredFields();
    }

    /**
     * Find not null fields
     *
     * @param clazz         Target class
     * @param bean          Target java bean instance
     * @param skipTypes     Type list which not deal
     * @param checkNotEmpty Enable to check not empty fields for collection and map
     * @return field metadata and value of not null field
     */
    public static Map<Field, Object> findNotNullFieldValue(Class<?> clazz,
                                                           Object bean,
                                                           Set<Class<?>> skipTypes,
                                                           Set<String> skipFields,
                                                           boolean checkNotEmpty) {
        // find not null fields
        Field[] fields = getFields(clazz);
        Map<Field, Object> notNullFields = new HashMap<>(fields.length);

        if (skipTypes == null) {
            skipTypes = new HashSet<>(0);
        }

        if (skipFields == null) {
            skipFields = new HashSet<>(0);
        }

        for (Field field : fields) {
            if (skipFields.contains(field.getName()) || skipTypes.contains(field.getType())) {
                continue;
            }

            try {
                String fieldName = field.getName();
                fieldName = fieldNameForSetterGetter(fieldName);
                Method method = clazz.getDeclaredMethod("get" + fieldName);

                Object value = method.invoke(bean);
                if (value == null) {
                    continue;
                }

                if (checkNotEmpty && value instanceof Collection) {
                    Collection tmp = (Collection) value;
                    if (tmp.size() > 0) {
                        notNullFields.put(field, value);
                    }
                    continue;
                }

                if (checkNotEmpty && value instanceof Map) {
                    Map tmp = (Map) value;
                    if (tmp.size() > 0) {
                        notNullFields.put(field, value);
                    }
                    continue;
                }

                notNullFields.put(field, value);

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                continue;
            }
        }

        return notNullFields;
    }

    /**
     * Deep copy object by byte array stream
     *
     * @param source
     * @param <T>
     * @return
     */
    public static <T> T deepCopy(T source) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(source);
                oos.flush();
            }

            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()))) {
                return (T) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    private static String fieldNameForSetterGetter(String fieldName) {
        return Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }
}