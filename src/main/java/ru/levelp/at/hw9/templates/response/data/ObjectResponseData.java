package ru.levelp.at.hw9.templates.response.data;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@SuperBuilder
@ToString
public class ObjectResponseData {

    private Long id;

}
