package ru.levelp.at.hw9.templates;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Status {

    ACTIVE("active"),
    INACTIVE("inactive"),
    INVALID("qwerty"),
    BLANK("    "),
    EMPTY("");

    @JsonValue
    private final String value;

}
