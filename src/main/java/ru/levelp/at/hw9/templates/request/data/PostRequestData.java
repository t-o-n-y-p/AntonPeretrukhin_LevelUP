package ru.levelp.at.hw9.templates.request.data;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class PostRequestData {

    @JsonProperty("user_id")
    private Long userId;
    private String title;
    private String body;

}
