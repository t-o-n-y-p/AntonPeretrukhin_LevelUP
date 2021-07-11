package ru.levelp.at.hw9.templates.response.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@SuperBuilder
public class PostResponseData extends ObjectResponseData {

    @JsonProperty("user_id")
    private Long userId;
    private String title;
    private String body;

}
