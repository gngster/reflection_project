package com.zuhlke.reflection.factory;

public class GenderType implements Type {
    @Override
    public Object getTypedValue(String value) {
        return Gender.valueOf(value);
    }
}
