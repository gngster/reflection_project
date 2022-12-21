import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;


public class Reflection {
    public Reflection() {
    }

    // Spring uses reflection to find annotations, expensive to grab the annotations and initialise the beans etc.
    // micronaut (do it at compile time instead of runtime like spring)
    // open api analyse the code in controller annotated class and generates the yaml file based on analysed methods.

    // refactor into clearer methods
    // 1. createInstanceOfClass(T clazz)
    // 2. parseText(text) (use a map instead of a list so it's clearer)
    // 3. setValuesToFields(fieldsAndValues)


    // catching exceptions:
    // be precise with exceptions
    // we should not do log and throw exception, anti-pattern
    // logging error is a way of handling the exception.
    // throwing exception is a way of passing the problem to the method calling it.
    // if you log errors at every catch block, you may end up having multiple exceptions to
    // if you logged it (a way of handling it), you should not throw it.
    public static <T> T parse(String text, Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        T newClass = clazz.getConstructor().newInstance();

        String[] parts = text.split("[{},:]");
        List<String> keyValueList = Arrays.stream(parts).filter(s -> s.length() > 0).toList(); // don't repeat the type in the naming, just put as keyValues, people would expect a map, so you should use a map, or either change the name...

        for (int i = 0; i < (long) keyValueList.size(); i += 2) {
            Field field = getField(keyValueList.get(i), newClass);
            // String fieldValue = keyValueList.get(i + 1);
            setField(field, newClass, keyValueList.get(i + 1)); // change the order from object to field to actual value or other way around (clearer for understanding purpose)
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
            // open closed principle -
            // dont touch existing code, cos you will break code, open for extension, closed for modification
            // factory implementation , no business logic in factory so not so tragic, shouldnt have biz logic in factory.just switch statements
            // when you see a bigger if else block, start to think about using a design pattern (factory/strategy, etc)
            // extract the if else block and make use of a switch case
            // unit test that method
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

    // comments:
    // have packages
    // separate source implementation and tests
    // look at warnings in IDE
}