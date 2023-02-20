package com.zuhlke.reflection.factory;

public class IntegerType implements Type {
    @Override
    public Object getTypedValue(String value) {
        return Integer.parseInt(value);
    }
}
