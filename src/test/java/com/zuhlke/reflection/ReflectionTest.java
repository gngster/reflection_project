package com.zuhlke.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReflectionTest {

    @Nested
    @DisplayName("Creating Person object")
    class CreatePersonObject {
        @Test
        @DisplayName("when parse one string value, Person is created")
        public void parseOneStringValueToPersonClass() throws Exception {
            Person person = Reflection.parse("name:steffie", Person.class);

            assertThat(person.getName()).isEqualTo("steffie");

        }

        @Test
        @DisplayName("when parse one integer value, Person is created")
        public void parseOneIntegerValueToPersonClass() throws Exception {
            Person person = Reflection.parse("age:28", Person.class);

            assertThat(person.getAge()).isEqualTo(28);
        }

        @Test
        @DisplayName("when parse all valid values, person is created")
        public void parseAllValidValuesToPersonClass() throws Exception {
            Person person = Reflection.parse("name:Steffie,age:28,gender:F", Person.class);

            assertThat(person.getName()).isEqualTo("Steffie");
            assertThat(person.getAge()).isEqualTo(28);
            assertThat(person.getGender().getGenderDescr()).isEqualTo("female");
        }

        @Test
        @DisplayName("when parsing blank spaces to person class, value will be trimmed to empty string")
        public void parseBlankSpacesToPersonClass() throws Exception {
            Person person = Reflection.parse("name:   ", Person.class);

            assertThat(person.getName()).isEqualTo("");
        }

        @Test
        @DisplayName("when parsing null to person class, value will be set to null")
        public void parseNullValuesToPersonClass() throws Exception {
            Person person = Reflection.parse("name:null", Person.class);

            assertThat(person.getName()).isNull();
        }

        @Test
        @DisplayName("when parse json notation, person is created")
        public void parseJsonNotationToPersonClassWithFilledValues() throws Exception {
            Person person = Reflection.parse("{\"name\":\"Steffie\", \"age\":28, \"gender\":\"F\"}", Person.class);

            assertThat(person.getName()).isEqualTo("Steffie");
            assertThat(person.getAge()).isEqualTo(28);
            assertThat(person.getGender().getGenderDescr()).isEqualTo("female");
        }

        @Test
        @DisplayName("when parse json notation with null and empty values, person is created")
        public void parseJsonNotationToPersonClassWithNullAndEmpty() throws Exception {
            Person person = Reflection.parse("{\"name\":\"Steffie\", \"age\":null, \"gender\":\"M\"}", Person.class);

            assertThat(person.getName()).isEqualTo("Steffie");
            assertThat(person.getAge()).isNull();
            assertThat(person.getGender().getGenderDescr()).isEqualTo("male");
        }
    }

    @Nested
    @DisplayName("Errors")
    class TestErrors {
        @Test
        @DisplayName("when parse invalid field names, error is thrown")
        public void parseInvalidFieldNamesToPersonClass() {
            assertThatThrownBy(() -> Reflection.parse("currency:Euros,price:100.39", Person.class)).isInstanceOf(NoSuchFieldException.class);
        }

        @Test
        @DisplayName("when parse invalid integer, error thrown")
        public void parseInvalidFieldToPersonClass() {
            assertThatThrownBy(() -> Reflection.parse("name:Steffie,age:invalidAge,gender:F", Person.class)).isInstanceOf(NumberFormatException.class);
        }

    }

    @Nested
    @DisplayName("Creating Price object")
    class TestPriceObject {
        @Test
        @DisplayName("when parse one string value, Price is created")
        public void parseOneStringValueToPriceClass() throws Exception {
            Price price = Reflection.parse("currency:Euros", Price.class);

            assertThat(price.getCurrency()).isEqualTo("Euros");
        }

        @Test
        @DisplayName("when parse one double value, Price is created")
        public void parseOneDoubleValueToPriceClass() throws Exception {
            Price price = Reflection.parse("value:100.39", Price.class);

            assertThat(price.getValue()).isEqualTo(100.39);
        }

        @Test
        @DisplayName("when parse all valid values, price is created")
        public void parseAllValidValuesToPriceClass() throws Exception {
            Price price = Reflection.parse("currency:Euros,value:100.39", Price.class);

            assertThat(price.getCurrency()).isEqualTo("Euros");
            assertThat(price.getValue()).isEqualTo(100.39);
        }
    }

//    @Nested
//    @DisplayName("parseText method")
//    class TestParseText {
//        @Test
//        @DisplayName("given valid string with colons ")
//        public void testValidString()
//    }

    @Nested
    @DisplayName("getTypedValue method")
    class TestGetTypedValue {
        @Test
        @DisplayName("when parse string, getTypedValue should return String")
        public void testGetTypedValueString() {
            Object string = Reflection.getTypedValue("STRING", "Steffie");

            assertThat(string)
                    .isInstanceOf(String.class)
                    .isEqualTo("Steffie");
        }

        @Test
        @DisplayName("when parse integer, getTypedValue should return Integer")
        public void testGetTypedValueInteger() {
            Object integer = Reflection.getTypedValue("INTEGER", "28");

            assertThat(integer)
                    .isInstanceOf(Integer.class)
                    .isEqualTo(28);
        }

        @Test
        @DisplayName("when parse double, getTypedValue should return Double")
        public void testGetTypedValueDouble() {
            Object integer = Reflection.getTypedValue("DOUBLE", "28.00");

            assertThat(integer)
                    .isInstanceOf(Double.class)
                    .isEqualTo(28.00);
        }

        @Test
        @DisplayName("when parse boolean, getTypedValue should throw exception")
        public void testGetTypedValueBoolean() {
            assertThatThrownBy(() -> Reflection.getTypedValue("BOOLEAN", "28.00")).isInstanceOf(IllegalArgumentException.class);
        }
    }
}