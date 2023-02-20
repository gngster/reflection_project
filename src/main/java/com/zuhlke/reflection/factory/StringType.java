package com.zuhlke.reflection.factory;

public class StringType implements Type {

    @Override
    public Object getTypedValue(String value) {
        return value;
    }
}
