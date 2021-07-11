package ru.levelp.at.hw9.templates.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Builder
public class ResponseBody<T> {

    private Metadata meta;
    private T data;

    @SneakyThrows
    public static <T> ResponseBody<T> createFromString(String responseBody, Class<T> responseDataClass) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(responseBody);
        return ResponseBody.<T>builder()
            .meta(mapper.readValue(node.get("meta").toString(), Metadata.class))
            .data(mapper.readValue(node.get("data").toString(), responseDataClass))
            .build();
    }

    @SneakyThrows
    public static ResponseBody<?> createFromString(String responseBody, CollectionType collectionType) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(responseBody);
        return ResponseBody.builder()
            .meta(mapper.readValue(node.get("meta").toString(), Metadata.class))
            .data(mapper.readValue(node.get("data").toString(), collectionType))
            .build();
    }

}
