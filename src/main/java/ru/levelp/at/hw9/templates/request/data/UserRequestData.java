package ru.levelp.at.hw9.templates.request.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.levelp.at.hw9.templates.Gender;
import ru.levelp.at.hw9.templates.Status;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Builder
public class UserRequestData {

    private String name;
    private Gender gender;
    private String email;
    private Status status;

}
