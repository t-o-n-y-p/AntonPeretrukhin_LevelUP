package ru.levelp.at.hw9.templates.response.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.levelp.at.hw9.templates.Gender;
import ru.levelp.at.hw9.templates.Status;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
@SuperBuilder
public class UserResponseData extends ObjectResponseData {

    private String name;
    private Gender gender;
    private String email;
    private Status status;

}
