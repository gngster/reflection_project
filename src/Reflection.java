import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;


public class Reflection {
    public Reflection() {
    }

    public static <T> T parse(String text, Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        T newClass = clazz.getConstructor().newInstance();

        String[] parts = text.split("[{},:]");
        List<String> keyValueList = Arrays.stream(parts).filter(s -> s.length() > 0).toList();

        for (int i = 0; i < (long) keyValueList.size(); i += 2) {
            Field field = getField(keyValueList.get(i), newClass);
            setField(field, newClass, keyValueList.get(i + 1));
        }

        return newClass;
    }

    private static void setField(Field field, Object newClass, String value) throws IllegalAccessException {
        Class<?> fieldType = field.getType();
        String fieldTypeName = fieldType.getTypeName();
        String validValue = getValidFieldOrValue(value);

        if (validValue.equals("null")) {
            field.set(newClass, null);
        } else {
            if (fieldTypeName.equals(String.class.getTypeName())) {
                field.set(newClass, validValue);
            }
            if (fieldTypeName.equals(Integer.class.getTypeName())) {
                field.set(newClass, Integer.parseInt(validValue));
            }
            if (fieldTypeName.equals(Double.class.getTypeName())) {
                field.set(newClass, Double.parseDouble(validValue));
            }
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
}