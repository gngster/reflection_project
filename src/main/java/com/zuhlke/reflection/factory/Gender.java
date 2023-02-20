package com.zuhlke.reflection.factory;

public enum Gender {
    F("female"), M("male"), U("unknown");
    private final String gender;

    public String getGenderDescr() {
        return gender;
    }

    Gender(String gender) {
        this.gender = gender;
    }
}
