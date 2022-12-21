import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class ReflectionTest {
// comments:
    // use given when then in display name if using display name, add more info
    // dont need to list all the exceptions in test, just throw exception.
    // init (data, mocks, etc)
    // execute the method
    // assert values are correct
    // put empty lines between the 3 steps
    // learn assertj - better error message, convenient methods to check list and array. containsInOrder
    @Test
    @DisplayName("parse one string value to person class")
    public void parseOneStringValueToPersonClass() throws InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
        Person person = Reflection.parse("name:steffie", Person.class);

        assertEquals("steffie", person.getName());
        assertNull(person.getAge());
        assertNull(person.getGender());
    }

    @Test
    @DisplayName("parse one string value to price class")
    public void parseOneStringValueToPriceClass() throws InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
        Price price = Reflection.parse("currency:Euros", Price.class);
        assertEquals("Euros", price.getCurrency());
        assertNull(price.getValue());
    }

    @Test
    @DisplayName("parse one integer value to person class")
    public void parseOneIntegerValueToPersonClass() throws InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
        Person person = Reflection.parse("age:28", Person.class);
        assertEquals(28, person.getAge());
        assertNull(person.getName());
        assertNull(person.getGender());
    }

    @Test
    @DisplayName("parse one double value to price class")
    public void parseOneDoubleValueToPriceClass() throws InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
        Price price = Reflection.parse("value:100.39", Price.class);
        assertEquals(100.39, price.getValue());
        assertNull(price.getCurrency());
    }

    @Test
    @DisplayName("parse all valid values to person class")
    public void parseAllValidValuesToPersonClass() throws InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
        Person person = Reflection.parse("name:Steffie,age:28,gender:female", Person.class);
        assertEquals("Steffie", person.getName());
        assertEquals(28, person.getAge());
        assertEquals("female", person.getGender());
    }

    @Test
    @DisplayName("parse all valid values to price class")
    public void parseAllValidValuesToPriceClass() throws InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
        Price price = Reflection.parse("currency:Euros,value:100.39", Price.class);
        assertEquals("Euros", price.getCurrency());
        assertEquals(100.39, price.getValue());
    }

    @Test
    @DisplayName("parse invalid integer field to person class")
    public void parseInvalidFieldToPersonClass() {
        assertThrows(NumberFormatException.class, () -> Reflection.parse("name:Steffie,age:invalidAge,gender:female", Person.class));
    }

    @Test
    @DisplayName("when parsing blank spaces to person class, value will be trimmed to empty string")
    public void parseBlankSpacesToPersonClass() throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Person person = Reflection.parse("name:   ,age:28,gender:  ", Person.class);
        assertEquals("", person.getName());
        assertEquals(28, person.getAge());
        assertEquals("", person.getGender());
    }

    @Test
    @DisplayName("when parsing null to person class, value will be set to null")
    public void parseNullValuesToPersonClass() throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Person person = Reflection.parse("name:null", Person.class);
        assertNull(person.getName());
        assertNull(person.getAge());
        assertNull(person.getGender());
    }

    @Test
    @DisplayName("parse invalid field names to person class")
    public void parseInvalidFieldNamesToPersonClass() {
        assertThrows(NoSuchFieldException.class, () -> Reflection.parse("currency:Euros,price:100.39", Person.class));
    }

    @Test
    @DisplayName("parse json notation to person class with filled values")
    public void parseJsonNotationToPersonClassWithFilledValues() throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Person person = Reflection.parse("{\"name\":\"Steffie\", \"age\":28, \"gender\":\"female\"}", Person.class);
        assertEquals("Steffie", person.getName());
        assertEquals(28, person.getAge());
        assertEquals("female", person.getGender());
    }

    @Test
    @DisplayName("parse json notation to person class with null and empty values")
    public void parseJsonNotationToPersonClassWithNullAndEmpty() throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Person person = Reflection.parse("{\"name\":\"Steffie\", \"age\":null, \"gender\":\"\"}", Person.class);
        assertEquals("Steffie", person.getName());
        assertNull(person.getAge());
        assertEquals("", person.getGender());
    }
}