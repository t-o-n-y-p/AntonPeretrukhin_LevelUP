package ru.levelp.at.hw9.templates.request.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Builder
public class CommentRequestData {

    @JsonProperty("post_id")
    private Long postId;
    private String name;
    private String email;
    private String body;

}
