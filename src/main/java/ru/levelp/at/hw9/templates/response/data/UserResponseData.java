package ru.levelp.at.hw9.templates.response.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.levelp.at.hw9.templates.Gender;
import ru.levelp.at.hw9.templates.Status;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@SuperBuilder
@ToString
public class UserResponseData extends ObjectResponseData {

    private String name;
    private Gender gender;
    private String email;
    private Status status;

}
