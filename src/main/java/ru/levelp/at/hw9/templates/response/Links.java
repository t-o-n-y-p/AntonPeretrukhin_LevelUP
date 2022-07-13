package ru.levelp.at.hw9.templates.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Builder
@ToString
public class Links {

    private String previous;
    private String current;
    private String next;

}
