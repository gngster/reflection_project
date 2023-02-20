package com.zuhlke.reflection;

import com.zuhlke.reflection.factory.TypeFactory;
import com.zuhlke.reflection.factory.Type;
import com.zuhlke.reflection.factory.ValueTypeName;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Reflection {
    public Reflection() {
    }

    public static <T> T parse(String text, Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        T newClass = createClassInstance(clazz);

        HashMap<String, String> keyValues = parseText(text);

        setValueToFieldInClass(newClass, keyValues);

        return newClass;
    }

    private static <T> T createClassInstance(Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return clazz.getConstructor().newInstance();
    }

    static HashMap<String, String> parseText(String text) {
        HashMap<String, String> keyValues = new HashMap<>();
        String[] parts = text.split("[{},]");

        Arrays.stream(parts).filter(s -> s.length() > 0).forEach(
                s -> {
                    String[] splitKeyValues = s.split(":");
                    keyValues.put(splitKeyValues[0], splitKeyValues[1]);
                }
        );

       return keyValues;
    }

    private static void setValueToFieldInClass(Object newClass, HashMap<String, String> keyValues) throws NoSuchFieldException, IllegalAccessException {
        for (Map.Entry<String, String> pair : keyValues.entrySet()) {
            Field field = getField(pair.getKey(), newClass);
            setField(field, newClass, pair.getValue());
        }
    }

    private static void setField(Field field, Object newClass, String value) throws IllegalAccessException {
        Class<?> fieldType = field.getType();
        String validValue = getValidFieldOrValue(value);

        if (validValue.equals("null")) {
            field.set(newClass, null);
        } else {
            Object typedValue = getTypedValue(fieldType.getSimpleName(), validValue);
            field.set(newClass, typedValue);
        }
    }

    private static String getValidFieldOrValue(String fieldOrValue) {
        return fieldOrValue.replace("\"", "").trim();
    }

    private static Field getField(String fieldName, Object newClass) throws NoSuchFieldException {
        String validFieldName = getValidFieldOrValue(fieldName);
        Field field = newClass.getClass().getDeclaredField(validFieldName);
        field.setAccessible(true);
        return field;
    }

    static Object getTypedValue(String fieldType, String validValue) {
        TypeFactory typeFactory = new TypeFactory();
        Type type = typeFactory.getType(ValueTypeName.valueOf(fieldType.toUpperCase()));
        return type.getTypedValue(validValue);
    }
}

