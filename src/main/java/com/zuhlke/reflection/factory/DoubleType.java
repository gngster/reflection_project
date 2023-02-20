package com.zuhlke.reflection.factory;

public class DoubleType implements Type {
    @Override
    public Object getTypedValue(String value) {
        return Double.parseDouble(value);
    }
}
