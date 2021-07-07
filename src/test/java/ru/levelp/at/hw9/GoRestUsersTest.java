package ru.levelp.at.hw9;

import static io.restassured.RestAssured.given;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.common.mapper.TypeRef;
import io.restassured.internal.mapping.Jackson2Mapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.levelp.at.hw9.templates.Gender;
import ru.levelp.at.hw9.templates.Status;
import ru.levelp.at.hw9.templates.request.data.UserRequestData;
import ru.levelp.at.hw9.templates.response.Metadata;
import ru.levelp.at.hw9.templates.response.Pagination;
import ru.levelp.at.hw9.templates.response.ResponseBody;
import ru.levelp.at.hw9.templates.response.data.ErrorMessageResponseData;
import ru.levelp.at.hw9.templates.response.data.ErrorResponseData;
import ru.levelp.at.hw9.templates.response.data.UserResponseData;

public class GoRestUsersTest extends BaseTest {

    @DataProvider
    private Object[] getGetUserData() {
        Object[][] data = new Object[][] {
            {faker.name().fullName(), Gender.MALE, faker.internet().emailAddress(), Status.ACTIVE},
            {faker.name().fullName(), Gender.FEMALE, faker.internet().emailAddress(), Status.INACTIVE}
        };
        return Arrays.stream(data)
            .map(e -> UserRequestData.builder()
                .name((String) e[0])
                .gender((Gender) e[1])
                .email((String) e[2])
                .status((Status) e[3])
                .build()
            ).toArray();
    }

    @DataProvider
    private Object[][] getAddUserData() {
        Object[][] data = new Object[][] {
            {faker.name().fullName(), Gender.MALE, faker.internet().emailAddress(), Status.ACTIVE},
            {faker.name().fullName(), Gender.FEMALE, faker.internet().emailAddress(), Status.INACTIVE}
        };
        return Arrays.stream(data)
            .map(e -> new Object[]{
                UserRequestData.builder()
                    .name((String) e[0])
                    .gender((Gender) e[1])
                    .email((String) e[2])
                    .status((Status) e[3])
                    .build(),
                ResponseBody.<UserResponseData>builder()
                    .code(201)
                    .meta(null)
                    .data(
                        UserResponseData.builder()
                            .name((String) e[0])
                            .gender((Gender) e[1])
                            .email((String) e[2])
                            .status((Status) e[3])
                            .build()
                    )
                    .build()
            })
            .toArray(Object[][]::new);
    }

    @DataProvider
    private Object[][] getAddUserNegativeData() {
        return new Object[][]{
            {
                UserRequestData.builder()
                    .name("")
                    .gender(null)
                    .email("qwerty")
                    .status(Status.BLANK)
                    .build(),
                ResponseBody.<List<ErrorResponseData>>builder()
                    .code(422)
                    .meta(null)
                    .data(List.of(
                        new ErrorResponseData("name", "can't be blank"),
                        new ErrorResponseData("gender", "can't be blank"),
                        new ErrorResponseData("email", "is invalid"),
                        new ErrorResponseData("status", "can't be blank")
                    ))
                    .build()
            },
            {
                UserRequestData.builder()
                    .name("    ")
                    .gender(Gender.EMPTY)
                    .email(null)
                    .status(Status.INVALID)
                    .build(),
                ResponseBody.<List<ErrorResponseData>>builder()
                    .code(422)
                    .meta(null)
                    .data(List.of(
                        new ErrorResponseData("name", "can't be blank"),
                        new ErrorResponseData("gender", "can't be blank"),
                        new ErrorResponseData("email", "can't be blank"),
                        new ErrorResponseData("status", "can be Active or Inactive")
                    ))
                    .build()
            },
            {
                UserRequestData.builder()
                    .name("0".repeat(10000))
                    .gender(Gender.BLANK)
                    .email("")
                    .status(null)
                    .build(),
                ResponseBody.<List<ErrorResponseData>>builder()
                    .code(422)
                    .meta(null)
                    .data(List.of(
                        new ErrorResponseData("name", "is too long (maximum is 200 characters)"),
                        new ErrorResponseData("gender", "can't be blank"),
                        new ErrorResponseData("email", "can't be blank"),
                        new ErrorResponseData("status", "can't be blank")
                    ))
                    .build()
            },
            {
                UserRequestData.builder()
                    .name(null)
                    .gender(Gender.INVALID)
                    .email("    ")
                    .status(Status.EMPTY)
                    .build(),
                ResponseBody.<List<ErrorResponseData>>builder()
                    .code(422)
                    .meta(null)
                    .data(List.of(
                        new ErrorResponseData("name", "can't be blank"),
                        new ErrorResponseData("gender", "can be Male or Female"),
                        new ErrorResponseData("email", "can't be blank"),
                        new ErrorResponseData("status", "can't be blank")
                    ))
                    .build()
            }
        };
    }

    @DataProvider
    private Object[][] getAddUserMissingFieldsData() {
        String email = faker.internet().emailAddress();
        String name = faker.name().fullName();
        return new Object[][]{
            {
                UserRequestData.builder()
                    .gender(Gender.FEMALE)
                    .email(email)
                    .status(Status.ACTIVE)
                    .build(),
                ResponseBody.<List<ErrorResponseData>>builder()
                    .code(422)
                    .meta(null)
                    .data(List.of(
                        new ErrorResponseData("name", "can't be blank")
                    ))
                    .build()
            },
            {
                UserRequestData.builder()
                    .name(name)
                    .email(email)
                    .status(Status.ACTIVE)
                    .build(),
                ResponseBody.<List<ErrorResponseData>>builder()
                    .code(422)
                    .meta(null)
                    .data(List.of(
                        new ErrorResponseData("gender", "can't be blank")
                    ))
                    .build()
            },
            {
                UserRequestData.builder()
                    .name(name)
                    .gender(Gender.FEMALE)
                    .status(Status.ACTIVE)
                    .build(),
                ResponseBody.<List<ErrorResponseData>>builder()
                    .code(422)
                    .meta(null)
                    .data(List.of(
                        new ErrorResponseData("email", "can't be blank")
                    ))
                    .build()
            },
            {
                UserRequestData.builder()
                    .name(name)
                    .gender(Gender.FEMALE)
                    .email(email)
                    .build(),
                ResponseBody.<List<ErrorResponseData>>builder()
                    .code(422)
                    .meta(null)
                    .data(List.of(
                        new ErrorResponseData("status", "can't be blank")
                    ))
                    .build()
            },
        };
    }

    @Test(dataProvider = "getAddUserData")
    public void testAddUser(UserRequestData requestBody, ResponseBody<UserResponseData> expectedResponseBody) {
        ResponseBody<UserResponseData> actualResponseBody = given()
            .body(requestBody)
            .when()
            .post("/public-api/users")
            .then()
            .statusCode(200)
            .extract()
            .as(new TypeRef<>(){});

        allCreatedUsers.add(actualResponseBody.getData().getId());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actualResponseBody)
                  .usingRecursiveComparison()
                  .ignoringExpectedNullFields()
                  .isEqualTo(expectedResponseBody);
            softly.assertThat(actualResponseBody)
                  .extracting(ResponseBody::getMeta)
                  .isNull();
            softly.assertThat(actualResponseBody)
                  .extracting(r -> r.getData().getId())
                  .isNotNull();
            softly.assertThat(actualResponseBody)
                  .extracting(r -> r.getData().getCreatedAt())
                  .isNotNull();
            softly.assertThat(actualResponseBody)
                  .extracting(r -> r.getData().getUpdatedAt())
                  .isNotNull();
        });
    }

    @Test(dataProvider = "getAddUserNegativeData")
    public void testAddUserNegative(
        UserRequestData requestBody,
        ResponseBody<List<ErrorResponseData>> expectedResponseBody
    ) {
        ResponseBody<List<ErrorResponseData>> actualResponseBody = given()
            .body(requestBody)
            .when()
            .post("/public-api/users")
            .then()
            .statusCode(200)
            .extract()
            .as(new TypeRef<>(){});

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actualResponseBody)
                  .usingRecursiveComparison()
                  .ignoringExpectedNullFields()
                  .ignoringCollectionOrder()
                  .isEqualTo(expectedResponseBody);
            softly.assertThat(actualResponseBody)
                  .extracting(ResponseBody::getMeta)
                  .isNull();
        });
    }

    @Test
    public void testAddUserEmailAlreadyExists() {
        String email = faker.internet().emailAddress();
        UserRequestData requestBody = UserRequestData.builder()
            .name(faker.name().fullName())
            .gender(Gender.FEMALE)
            .email(email)
            .status(Status.ACTIVE)
            .build();
        ResponseBody<UserResponseData> responseBody = given()
            .body(requestBody)
            .when()
            .post("/public-api/users")
            .then()
            .statusCode(200)
            .extract()
            .as(new TypeRef<>(){});
        allCreatedUsers.add(responseBody.getData().getId());

        requestBody = UserRequestData.builder()
            .name(faker.name().fullName())
            .gender(Gender.MALE)
            .email(email)
            .status(Status.INACTIVE)
            .build();
        ResponseBody<List<ErrorResponseData>> actualResponseBody = given()
            .body(requestBody)
            .when()
            .post("/public-api/users")
            .then()
            .statusCode(200)
            .extract()
            .as(new TypeRef<>(){});
        ResponseBody<List<ErrorResponseData>> expectedResponseBody = ResponseBody.<List<ErrorResponseData>>builder()
            .code(422)
            .meta(null)
            .data(List.of(
                new ErrorResponseData("email", "has already been taken")
            ))
            .build();
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actualResponseBody)
                  .usingRecursiveComparison()
                  .ignoringExpectedNullFields()
                  .ignoringCollectionOrder()
                  .isEqualTo(expectedResponseBody);
            softly.assertThat(actualResponseBody)
                  .extracting(ResponseBody::getMeta)
                  .isNull();
        });
    }

    @Test(dataProvider = "getAddUserMissingFieldsData")
    public void testAddUserMissingFields(
        UserRequestData requestBody,
        ResponseBody<List<ErrorResponseData>> expectedResponseBody
    ) {
        ResponseBody<List<ErrorResponseData>> actualResponseBody = given()
            .body(requestBody, new Jackson2Mapper(
                (type, s) -> new ObjectMapper().setSerializationInclusion(Include.NON_NULL)
            ))
            .when()
            .post("/public-api/users")
            .then()
            .statusCode(200)
            .extract()
            .as(new TypeRef<>(){});

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actualResponseBody)
                  .usingRecursiveComparison()
                  .ignoringExpectedNullFields()
                  .ignoringCollectionOrder()
                  .isEqualTo(expectedResponseBody);
            softly.assertThat(actualResponseBody)
                  .extracting(ResponseBody::getMeta)
                  .isNull();
        });
    }

    @Test
    public void testAddUserInvalidRequestBody() {
        given()
            .body("{}'")
            .when()
            .post("/public-api/users")
            .then()
            .statusCode(500);
    }

    @Test(dataProvider = "getGetUserData")
    public void testGetUser(UserRequestData requestBody) {
        ResponseBody<UserResponseData> expectedResponseBody = given()
            .body(requestBody)
            .when()
            .post("/public-api/users")
            .then()
            .statusCode(200)
            .extract()
            .as(new TypeRef<>(){});

        Long id = expectedResponseBody.getData().getId();
        allCreatedUsers.add(id);
        expectedResponseBody.setCode(200);
        ResponseBody<UserResponseData> actualResponseBody = given()
            .pathParam("id", id)
            .when()
            .get("/public-api/users/{id}")
            .then()
            .statusCode(200)
            .extract()
            .as(new TypeRef<>(){});

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actualResponseBody)
                  .usingRecursiveComparison()
                  .ignoringExpectedNullFields()
                  .isEqualTo(expectedResponseBody);
            softly.assertThat(actualResponseBody)
                  .extracting(ResponseBody::getMeta)
                  .isNull();
            softly.assertThat(actualResponseBody)
                  .extracting(r -> r.getData().getId())
                  .isNotNull();
            softly.assertThat(actualResponseBody)
                  .extracting(r -> r.getData().getCreatedAt())
                  .isNotNull();
            softly.assertThat(actualResponseBody)
                  .extracting(r -> r.getData().getUpdatedAt())
                  .isNotNull();
        });
    }

    @Test(dataProvider = "getGetUserData")
    public void testGetDeletedUser(UserRequestData requestBody) {
        ResponseBody<UserResponseData> responseBody = given()
            .body(requestBody)
            .when()
            .post("/public-api/users")
            .then()
            .statusCode(200)
            .extract()
            .as(new TypeRef<>(){});

        Long id = responseBody.getData().getId();
        given()
            .pathParam("id", id)
            .when()
            .delete("/public-api/users/{id}");

        ResponseBody<ErrorMessageResponseData> actualResponseBody = given()
            .pathParam("id", id)
            .when()
            .get("/public-api/users/{id}")
            .then()
            .statusCode(200)
            .extract()
            .as(new TypeRef<>(){});

        ResponseBody<ErrorMessageResponseData> expectedResponseBody = ResponseBody.<ErrorMessageResponseData>builder()
            .code(404)
            .meta(null)
            .data(new ErrorMessageResponseData("Resource not found"))
            .build();

        SoftAssertions.assertSoftly(softly -> softly.assertThat(actualResponseBody).isEqualTo(expectedResponseBody));
    }

    @Test
    public void testGetUserInvalidInput() {
        ResponseBody<ErrorMessageResponseData> actualResponseBody = given()
            .when()
            .get("/public-api/users/qwerty")
            .then()
            .statusCode(200)
            .extract()
            .as(new TypeRef<>(){});

        ResponseBody<ErrorMessageResponseData> expectedResponseBody = ResponseBody.<ErrorMessageResponseData>builder()
            .code(404)
            .meta(null)
            .data(new ErrorMessageResponseData("Resource not found"))
            .build();

        SoftAssertions.assertSoftly(softly -> softly.assertThat(actualResponseBody).isEqualTo(expectedResponseBody));
    }

    @Test
    public void testGetAllUsersFirstPage() {
        List<UserResponseData> expectedUsers = new ArrayList<>();
        int id = 1;
        while (expectedUsers.size() < 20) {
            ResponseBody<UserResponseData> responseBody = given()
                .pathParam("id", id)
                .when()
                .get("/public-api/users/{id}")
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<>(){});
            id++;
            if (responseBody.getCode() == 200) {
                expectedUsers.add(responseBody.getData());
            }
        }

        ResponseBody<List<UserResponseData>> actualResponseBody = given()
            .when()
            .get("/public-api/users")
            .then()
            .statusCode(200)
            .extract()
            .as(new TypeRef<>(){});
        ResponseBody<List<UserResponseData>> expectedResponseBody = ResponseBody.<List<UserResponseData>>builder()
            .code(200)
            .meta(new Metadata(Pagination.builder().page(1).limit(20).build()))
            .data(expectedUsers)
            .build();
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actualResponseBody)
                  .usingRecursiveComparison()
                  .ignoringExpectedNullFields()
                  .ignoringCollectionOrder()
                  .isEqualTo(expectedResponseBody);
            softly.assertThat(actualResponseBody)
                  .extracting(r -> r.getMeta().getPagination().getTotal())
                  .isNotNull();
            softly.assertThat(actualResponseBody)
                  .extracting(r -> r.getMeta().getPagination().getPages())
                  .isNotNull();
            softly.assertThat(actualResponseBody)
                  .extracting(r -> {
                      Pagination pagination = r.getMeta().getPagination();
                      return pagination.getTotal() <= pagination.getPages() * pagination.getLimit()
                          && pagination.getTotal() > (pagination.getPages() - 1) * pagination.getLimit();
                  })
                  .isEqualTo(true);
        });

    }

}
