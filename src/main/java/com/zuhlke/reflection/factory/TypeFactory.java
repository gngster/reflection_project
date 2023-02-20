package com.zuhlke.reflection.factory;

public class TypeFactory {
    public Type getType(ValueTypeName valueTypeName) {
        return switch (valueTypeName) {
            case STRING -> new StringType();
            case INTEGER -> new IntegerType();
            case DOUBLE -> new DoubleType();
            case GENDER -> new GenderType();
        };
    }
}
