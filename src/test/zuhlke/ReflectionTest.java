package test.zuhlke;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import main.zuhlke.Person;
import main.zuhlke.Price;
import main.zuhlke.Reflection;

import static org.assertj.core.api.Assertions.*;

class ReflectionTest extends Reflection {
    @Test
    @DisplayName("when parse one string value, Person is created")
    public void parseOneStringValueToPersonClass() throws Exception {
        Person person = Reflection.parse("name:steffie", Person.class);

        assertThat(person.getName()).isEqualTo("steffie");
        assertThat(person.getAge()).isNull();
        assertThat(person.getGender()).isNull();
    }

    @Test
    @DisplayName("when parse one string value, Price is created")
    public void parseOneStringValueToPriceClass() throws Exception {
        Price price = Reflection.parse("currency:Euros", Price.class);

        assertThat(price.getCurrency()).isEqualTo("Euros");
        assertThat(price.getValue()).isNull();
    }

    @Test
    @DisplayName("when parse one integer value, Person is created")
    public void parseOneIntegerValueToPersonClass() throws Exception {
        Person person = Reflection.parse("age:28", Person.class);

        assertThat(person.getAge()).isEqualTo(28);
        assertThat(person.getName()).isNull();
        assertThat(person.getGender()).isNull();
    }

    @Test
    @DisplayName("when parse one double value, Price is created")
    public void parseOneDoubleValueToPriceClass() throws Exception {
        Price price = Reflection.parse("value:100.39", Price.class);

        assertThat(price.getValue()).isEqualTo(100.39);
        assertThat(price.getCurrency()).isNull();
    }

    @Test
    @DisplayName("when parse all valid values, person is created")
    public void parseAllValidValuesToPersonClass() throws Exception {
        Person person = Reflection.parse("name:Steffie,age:28,gender:female", Person.class);

        assertThat(person.getName()).isEqualTo("Steffie");
        assertThat(person.getAge()).isEqualTo(28);
        assertThat(person.getGender()).isEqualTo("female");
    }

    @Test
    @DisplayName("when parse all valid values, price is created")
    public void parseAllValidValuesToPriceClass() throws Exception {
        Price price = Reflection.parse("currency:Euros,value:100.39", Price.class);

        assertThat(price.getCurrency()).isEqualTo("Euros");
        assertThat(price.getValue()).isEqualTo(100.39);
    }

    @Test
    @DisplayName("when parse invalid integer, error thrown")
    public void parseInvalidFieldToPersonClass() {
        assertThatThrownBy(() -> Reflection.parse("name:Steffie,age:invalidAge,gender:female", Person.class)).isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("when parsing blank spaces to person class, value will be trimmed to empty string")
    public void parseBlankSpacesToPersonClass() throws Exception {
        Person person = Reflection.parse("name:   ,age:28,gender:  ", Person.class);

        assertThat(person.getName()).isEqualTo("");
        assertThat(person.getAge()).isEqualTo(28);
        assertThat(person.getGender()).isEqualTo("");
    }

    @Test
    @DisplayName("when parsing null to person class, value will be set to null")
    public void parseNullValuesToPersonClass() throws Exception {
        Person person = Reflection.parse("name:null", Person.class);

        assertThat(person.getName()).isNull();
        assertThat(person.getAge()).isNull();
        assertThat(person.getGender()).isNull();
    }

    @Test
    @DisplayName("when parse invalid field names, error is thrown")
    public void parseInvalidFieldNamesToPersonClass() {
        assertThatThrownBy(() -> Reflection.parse("currency:Euros,price:100.39", Person.class)).isInstanceOf(NoSuchFieldException.class);
    }

    @Test
    @DisplayName("when parse json notation, person is created")
    public void parseJsonNotationToPersonClassWithFilledValues() throws Exception {
        Person person = Reflection.parse("{\"name\":\"Steffie\", \"age\":28, \"gender\":\"female\"}", Person.class);

        assertThat(person.getName()).isEqualTo("Steffie");
        assertThat(person.getAge()).isEqualTo(28);
        assertThat(person.getGender()).isEqualTo("female");
    }

    @Test
    @DisplayName("when parse json notation with null and empty values, person is created")
    public void parseJsonNotationToPersonClassWithNullAndEmpty() throws Exception {
        Person person = Reflection.parse("{\"name\":\"Steffie\", \"age\":null, \"gender\":\"\"}", Person.class);

        assertThat(person.getName()).isEqualTo("Steffie");
        assertThat(person.getAge()).isNull();
        assertThat(person.getGender()).isEqualTo("");
    }

    @Test
    @DisplayName("when parse string, getTypedValue should return String")
    public void testGetTypedValueString() {
        Object string = Reflection.getTypedValue("STRING", "Steffie");

        assertThat(string).isEqualTo("Steffie");
    }

    @Test
    @DisplayName("when parse integer, getTypedValue should return Integer")
    public void testGetTypedValueInteger() {
        Object integer = Reflection.getTypedValue("INTEGER", "28");

        assertThat(integer).isEqualTo(28);
    }

    @Test
    @DisplayName("when parse double, getTypedValue should return Double")
    public void testGetTypedValueDouble() {
        Object integer = Reflection.getTypedValue("DOUBLE", "28.00");

        assertThat(integer).isEqualTo(28.00);
    }

    @Test
    @DisplayName("when parse boolean, getTypedValue should throw exception")
    public void testGetTypedValueBoolean() {
        assertThatThrownBy(() -> Reflection.getTypedValue("BOOLEAN", "28.00")).isInstanceOf(IllegalArgumentException.class);
    }
}