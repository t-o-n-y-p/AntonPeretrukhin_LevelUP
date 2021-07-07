package ru.levelp.at.hw9.templates.response.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentData {

    private Long id;
    @JsonProperty("post_id")
    private Long postId;
    private String name;
    private String email;
    private String body;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;

}
