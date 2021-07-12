package ru.levelp.at.hw9.steps;

import static io.restassured.RestAssured.given;

import io.restassured.mapper.ObjectMapper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import ru.levelp.at.hw9.RequestUriUtil;

public class ActionStep {

    public <T> ExtractableResponse<Response> postObject(T requestBody, int statusCode) {
        return given()
            .body(requestBody)
            .when()
            .post(RequestUriUtil.OBJECTS_API_URL.get(requestBody.getClass()))
            .then()
            .statusCode(statusCode)
            .extract();
    }

    public <T> ExtractableResponse<Response> postObject(T requestBody, int statusCode, ObjectMapper mapper) {
        return given()
            .body(requestBody, mapper)
            .when()
            .post(RequestUriUtil.OBJECTS_API_URL.get(requestBody.getClass()))
            .then()
            .statusCode(statusCode)
            .extract();
    }

    public <T> void postObjectInvalidRequestBody(Class<T> requestDataClass) {
        given()
            .body("{}'")
            .when()
            .post(RequestUriUtil.OBJECTS_API_URL.get(requestDataClass))
            .then()
            .statusCode(500);
    }

    public <T> ExtractableResponse<Response> getObject(Long id, Class<T> requestDataClass, int statusCode) {
        return given()
            .pathParam("id", id)
            .when()
            .get(RequestUriUtil.OBJECT_BY_ID_API_URL.get(requestDataClass))
            .then()
            .statusCode(statusCode)
            .extract();
    }

    public <T> ExtractableResponse<Response> getObjectWithInvalidInput(Class<T> requestDataClass) {
        return given()
            .when()
            .get(RequestUriUtil.OBJECT_BY_ID_INVALID_INPUT_API_URL.get(requestDataClass))
            .then()
            .statusCode(404)
            .extract();
    }

    public <T> ExtractableResponse<Response> getObjectsFirstPage(Class<T> requestDataClass) {
        return given()
            .when()
            .get(RequestUriUtil.OBJECTS_API_URL.get(requestDataClass))
            .then()
            .statusCode(200)
            .extract();
    }

    public <T> ExtractableResponse<Response> putObject(Long id, T requestBody, int statusCode) {
        return given()
            .pathParam("id", id)
            .body(requestBody)
            .when()
            .put(RequestUriUtil.OBJECT_BY_ID_API_URL.get(requestBody.getClass()))
            .then()
            .statusCode(statusCode)
            .extract();
    }

    public <T> ExtractableResponse<Response> putObject(Long id, T requestBody, int statusCode, ObjectMapper mapper) {
        return given()
            .pathParam("id", id)
            .body(requestBody, mapper)
            .when()
            .put(RequestUriUtil.OBJECT_BY_ID_API_URL.get(requestBody.getClass()))
            .then()
            .statusCode(statusCode)
            .extract();
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

    public <T> ExtractableResponse<Response> putObjectWithInvalidInput(Class<T> requestDataClass) {
        return given()
            .body("{}")
            .when()
            .put(RequestUriUtil.OBJECT_BY_ID_INVALID_INPUT_API_URL.get(requestDataClass))
            .then()
            .statusCode(404)
            .extract();
    }

    public <T> ExtractableResponse<Response> deleteObject(Long id, Class<T> requestDataClass, int statusCode) {
        return given()
            .pathParam("id", id)
            .when()
            .delete(RequestUriUtil.OBJECT_BY_ID_API_URL.get(requestDataClass))
            .then()
            .statusCode(statusCode)
            .extract();
    }

    public <T> ExtractableResponse<Response> deleteObjectWithInvalidInput(Class<T> requestDataClass) {
        return given()
            .when()
            .delete(RequestUriUtil.OBJECT_BY_ID_INVALID_INPUT_API_URL.get(requestDataClass))
            .then()
            .statusCode(404)
            .extract();
    }
}
