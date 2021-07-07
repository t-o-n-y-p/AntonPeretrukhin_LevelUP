package ru.levelp.at.hw9.templates.response.data;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Gender {

    MALE("Male"),
    FEMALE("Female");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }
}
