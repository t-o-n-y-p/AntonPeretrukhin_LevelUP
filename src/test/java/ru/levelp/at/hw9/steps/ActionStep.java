package ru.levelp.at.hw9.steps;

import static io.restassured.RestAssured.given;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.common.mapper.TypeRef;
import io.restassured.internal.mapping.Jackson2Mapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.SneakyThrows;
import ru.levelp.at.hw9.RequestUriUtil;
import ru.levelp.at.hw9.templates.response.Metadata;
import ru.levelp.at.hw9.templates.response.ResponseBody;
import ru.levelp.at.hw9.templates.response.data.ErrorMessageResponseData;
import ru.levelp.at.hw9.templates.response.data.ErrorResponseData;

public class ActionStep {

    @SuppressWarnings("unchecked")
    public <T, R> ResponseBody<R> postObject(T requestBody) {
        Class<R> responseClass = (Class<R>) RequestUriUtil.REQUEST_RESPONSE_MAP.get(requestBody.getClass());
        String responseBody = given()
            .body(requestBody)
            .when()
            .post(RequestUriUtil.OBJECTS_API_URL.get(requestBody.getClass()))
            .then()
            .statusCode(201)
            .extract()
            .jsonPath()
            .prettify();
        return ResponseBody.createFromString(responseBody, responseClass);
    }

    public <T> ResponseBody<List<ErrorResponseData>> postObjectWithErrors(T requestBody) {
        return given()
            .body(requestBody)
            .when()
            .post(RequestUriUtil.OBJECTS_API_URL.get(requestBody.getClass()))
            .then()
            .statusCode(422)
            .extract()
            .as(new TypeRef<>(){});
    }

    public <T> ResponseBody<List<ErrorResponseData>> postObjectWithErrorsWithoutNullValues(T requestBody) {
        return given()
            .body(requestBody, new Jackson2Mapper(
                (type, s) -> new ObjectMapper().setSerializationInclusion(Include.NON_NULL)
            ))
            .when()
            .post(RequestUriUtil.OBJECTS_API_URL.get(requestBody.getClass()))
            .then()
            .statusCode(422)
            .extract()
            .as(new TypeRef<>(){});
    }

    public <T> void postObjectInvalidRequestBody(Class<T> requestDataClass) {
        given()
            .body("{}'")
            .when()
            .post(RequestUriUtil.OBJECTS_API_URL.get(requestDataClass))
            .then()
            .statusCode(500);
    }

    @SuppressWarnings("unchecked")
    public <T, R> ResponseBody<R> getObject(Long id, Class<T> requestDataClass) {
        Class<R> responseClass = (Class<R>) RequestUriUtil.REQUEST_RESPONSE_MAP.get(requestDataClass);
        String responseBody = given()
            .pathParam("id", id)
            .when()
            .get(RequestUriUtil.OBJECT_BY_ID_API_URL.get(requestDataClass))
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .prettify();
        return ResponseBody.createFromString(responseBody, responseClass);
    }

    public <T> ResponseBody<ErrorMessageResponseData> getObjectWithError(Long id, Class<T> requestDataClass) {
        return given()
            .pathParam("id", id)
            .when()
            .get(RequestUriUtil.OBJECT_BY_ID_API_URL.get(requestDataClass))
            .then()
            .statusCode(404)
            .extract()
            .as(new TypeRef<>(){});
    }

    public <T> ResponseBody<ErrorMessageResponseData> getObjectWithInvalidInput(Class<T> requestDataClass) {
        return given()
            .when()
            .get(RequestUriUtil.OBJECT_BY_ID_INVALID_INPUT_API_URL.get(requestDataClass))
            .then()
            .statusCode(404)
            .extract()
            .as(new TypeRef<>(){});
    }

    @SuppressWarnings("unchecked")
    public <T, R> ResponseBody<List<R>> getObjectsFirstPage(Class<T> requestDataClass) {
        ObjectMapper mapper = new ObjectMapper();
        Class<R> responseClass = (Class<R>) RequestUriUtil.REQUEST_RESPONSE_MAP.get(requestDataClass);
        String responseBody = given()
            .when()
            .get(RequestUriUtil.OBJECTS_API_URL.get(requestDataClass))
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .prettify();
        return (ResponseBody<List<R>>) ResponseBody.createFromString(
            responseBody,
            mapper.getTypeFactory().constructCollectionType(List.class, responseClass)
        );
    }

    @SuppressWarnings("unchecked")
    public <T, R> ResponseBody<R> putObject(Long id, T requestBody) {
        Class<R> responseClass = (Class<R>) RequestUriUtil.REQUEST_RESPONSE_MAP.get(requestBody.getClass());
        String responseBody = given()
            .pathParam("id", id)
            .body(requestBody)
            .when()
            .put(RequestUriUtil.OBJECT_BY_ID_API_URL.get(requestBody.getClass()))
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .prettify();
        return ResponseBody.createFromString(responseBody, responseClass);
    }

    public <T> ResponseBody<List<ErrorResponseData>> putObjectWithErrors(Long id, T requestBody) {
        return given()
            .pathParam("id", id)
            .body(requestBody)
            .when()
            .put(RequestUriUtil.OBJECT_BY_ID_API_URL.get(requestBody.getClass()))
            .then()
            .statusCode(422)
            .extract()
            .as(new TypeRef<>(){});
    }

    public <T> ResponseBody<ErrorMessageResponseData> putObjectWithError(Long id, T requestBody) {
        return given()
            .pathParam("id", id)
            .body(requestBody)
            .when()
            .put(RequestUriUtil.OBJECT_BY_ID_API_URL.get(requestBody.getClass()))
            .then()
            .statusCode(404)
            .extract()
            .as(new TypeRef<>(){});
    }

    @SuppressWarnings("unchecked")
    public <T, R> ResponseBody<R> putObjectWithoutNullValues(Long id, T requestBody) {
        Class<R> responseClass = (Class<R>) RequestUriUtil.REQUEST_RESPONSE_MAP.get(requestBody.getClass());
        String responseBody = given()
            .pathParam("id", id)
            .body(requestBody, new Jackson2Mapper(
                (type, s) -> new ObjectMapper().setSerializationInclusion(Include.NON_NULL)
            ))
            .when()
            .put(RequestUriUtil.OBJECT_BY_ID_API_URL.get(requestBody.getClass()))
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .prettify();
        return ResponseBody.createFromString(responseBody, responseClass);
    }

    public <T> ResponseBody<List<ErrorResponseData>> putObjectWithErrorsWithoutNullValues(Long id, T requestBody) {
        return given()
            .pathParam("id", id)
            .body(requestBody, new Jackson2Mapper(
                (type, s) -> new ObjectMapper().setSerializationInclusion(Include.NON_NULL)
            ))
            .when()
            .put(RequestUriUtil.OBJECT_BY_ID_API_URL.get(requestBody.getClass()))
            .then()
            .statusCode(422)
            .extract()
            .as(new TypeRef<>(){});
    }

    public <T> void putObjectInvalidRequestBody(Long id, Class<T> requestDataClass) {
        given()
            .pathParam("id", id)
            .body("{}'")
            .when()
            .post(RequestUriUtil.OBJECT_BY_ID_API_URL.get(requestDataClass))
            .then()
            .statusCode(500);
    }

    public <T> ResponseBody<ErrorMessageResponseData> putObjectWithInvalidInput(Class<T> requestDataClass) {
        return given()
            .body("{}")
            .when()
            .put(RequestUriUtil.OBJECT_BY_ID_INVALID_INPUT_API_URL.get(requestDataClass))
            .then()
            .statusCode(404)
            .extract()
            .as(new TypeRef<>(){});
    }

    public <T> void deleteObject(Long id, Class<T> requestDataClass) {
        given()
            .pathParam("id", id)
            .when()
            .delete(RequestUriUtil.OBJECT_BY_ID_API_URL.get(requestDataClass))
            .then()
            .statusCode(204);
    }

    public <T> ResponseBody<ErrorMessageResponseData> deleteObjectWithError(Long id, Class<T> requestDataClass) {
        return given()
            .pathParam("id", id)
            .when()
            .delete(RequestUriUtil.OBJECT_BY_ID_API_URL.get(requestDataClass))
            .then()
            .statusCode(404)
            .extract()
            .as(new TypeRef<>(){});
    }

    public <T> ResponseBody<ErrorMessageResponseData> deleteObjectWithInvalidInput(Class<T> requestDataClass) {
        return given()
            .when()
            .delete(RequestUriUtil.OBJECT_BY_ID_INVALID_INPUT_API_URL.get(requestDataClass))
            .then()
            .statusCode(404)
            .extract()
            .as(new TypeRef<>(){});
    }
}
