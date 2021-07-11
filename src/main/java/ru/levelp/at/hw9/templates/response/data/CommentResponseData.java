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
public class CommentResponseData extends ObjectResponseData {

    @JsonProperty("post_id")
    private Long postId;
    private String name;
    private String email;
    private String body;

}
