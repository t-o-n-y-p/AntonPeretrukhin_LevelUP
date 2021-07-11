package ru.levelp.at.hw9.templates.response.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
@SuperBuilder
public class PostResponseData extends ObjectResponseData {

    @JsonProperty("user_id")
    private Long userId;
    private String title;
    private String body;

}
