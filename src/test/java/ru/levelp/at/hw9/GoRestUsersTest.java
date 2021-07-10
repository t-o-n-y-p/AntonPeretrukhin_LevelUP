package ru.levelp.at.hw9;

import static io.restassured.RestAssured.given;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.common.mapper.TypeRef;
import io.restassured.internal.mapping.Jackson2Mapper;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.levelp.at.hw9.templates.Gender;
import ru.levelp.at.hw9.templates.Status;
import ru.levelp.at.hw9.templates.request.data.UserRequestData;
import ru.levelp.at.hw9.templates.response.Links;
import ru.levelp.at.hw9.templates.response.Metadata;
import ru.levelp.at.hw9.templates.response.Pagination;
import ru.levelp.at.hw9.templates.response.ResponseBody;
import ru.levelp.at.hw9.templates.response.data.ErrorMessageResponseData;
import ru.levelp.at.hw9.templates.response.data.ErrorResponseData;
import ru.levelp.at.hw9.templates.response.data.UserResponseData;

public class GoRestUsersTest extends BaseTest {

    @DataProvider
    private Object[] getGetUserObjectData() {
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
    private Object[][] getPostUserObjectData() {
        return getUserObjectData();
    }

    @DataProvider
    private Object[][] getPutUserObjectData() {
        return getUserObjectData();
    }

    private Object[][] getUserObjectData() {
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
    private Object[][] getPostOrPutUserObjectNegativeData() {
        return new Object[][]{
            {
                UserRequestData.builder()
                    .name("")
                    .gender(null)
                    .email("qwerty")
                    .status(Status.BLANK)
                    .build(),
                ResponseBody.<List<ErrorResponseData>>builder()
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
    private Object[][] getPostUserObjectMissingFieldsData() {
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
                    .meta(null)
                    .data(List.of(
                        new ErrorResponseData("status", "can't be blank")
                    ))
                    .build()
            }
        };
    }

    @DataProvider
    private Object[][] getPutUserObjectMissingFieldsData() {
        Object[][] postData = new Object[][] {
            {faker.name().fullName(), Gender.MALE, faker.internet().emailAddress(), Status.ACTIVE},
            {faker.name().fullName(), Gender.FEMALE, faker.internet().emailAddress(), Status.INACTIVE},
            {faker.name().fullName(), Gender.MALE, faker.internet().emailAddress(), Status.ACTIVE},
            {faker.name().fullName(), Gender.FEMALE, faker.internet().emailAddress(), Status.INACTIVE}
        };
        Object[][] putData = new Object[][] {
            {null, Gender.MALE, faker.internet().emailAddress(), Status.INACTIVE},
            {faker.name().fullName(), null, faker.internet().emailAddress(), Status.INACTIVE},
            {faker.name().fullName(), Gender.FEMALE, null, Status.ACTIVE},
            {faker.name().fullName(), Gender.FEMALE, faker.internet().emailAddress(), null}
        };
        return IntStream.range(0, Integer.min(postData.length, putData.length))
            .mapToObj(i -> new Object[]{
                UserRequestData.builder()
                    .name((String) postData[i][0])
                    .gender((Gender) postData[i][1])
                    .email((String) postData[i][2])
                    .status((Status) postData[i][3])
                    .build(),
                UserRequestData.builder()
                    .name((String) putData[i][0])
                    .gender((Gender) putData[i][1])
                    .email((String) putData[i][2])
                    .status((Status) putData[i][3])
                    .build(),
                ResponseBody.<UserResponseData>builder()
                    .meta(null)
                    .data(
                        UserResponseData.builder()
                            .name((String) (putData[i][0] == null ? postData[i][0] : putData[i][0]))
                            .gender((Gender) (putData[i][1] == null ? postData[i][1] : putData[i][1]))
                            .email((String) (putData[i][2] == null ? postData[i][2] : putData[i][2]))
                            .status((Status) (putData[i][3] == null ? postData[i][3] : putData[i][3]))
                            .build()
                    )
                    .build()
            })
            .toArray(Object[][]::new);

    }

    // ------------------------------
    // TESTS
    // ------------------------------

    @Test(dataProvider = "getPostUserObjectData")
    public void testPostUserObject(UserRequestData requestBody, ResponseBody<UserResponseData> expectedResponseBody) {
        ResponseBody<UserResponseData> actualResponseBody = given()
            .body(requestBody)
            .when()
            .post(USERS_API_URL)
            .then()
            .statusCode(201)
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
        });
    }

    @Test(dataProvider = "getPostOrPutUserObjectNegativeData")
    public void testPostUserObjectNegative(
        UserRequestData requestBody,
        ResponseBody<List<ErrorResponseData>> expectedResponseBody
    ) {
        ResponseBody<List<ErrorResponseData>> actualResponseBody = given()
            .body(requestBody)
            .when()
            .post(USERS_API_URL)
            .then()
            .statusCode(422)
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
    public void testPostUserObjectEmailAlreadyExists() {
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
            .post(USERS_API_URL)
            .then()
            .statusCode(201)
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
            .post(USERS_API_URL)
            .then()
            .statusCode(422)
            .extract()
            .as(new TypeRef<>(){});
        ResponseBody<List<ErrorResponseData>> expectedResponseBody = ResponseBody.<List<ErrorResponseData>>builder()
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

    @Test(dataProvider = "getPostUserObjectMissingFieldsData")
    public void testPostUserObjectMissingFields(
        UserRequestData requestBody,
        ResponseBody<List<ErrorResponseData>> expectedResponseBody
    ) {
        ResponseBody<List<ErrorResponseData>> actualResponseBody = given()
            .body(requestBody, new Jackson2Mapper(
                (type, s) -> new ObjectMapper().setSerializationInclusion(Include.NON_NULL)
            ))
            .when()
            .post(USERS_API_URL)
            .then()
            .statusCode(422)
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
    public void testPostUserObjectInvalidRequestBody() {
        given()
            .body("{}'")
            .when()
            .post(USERS_API_URL)
            .then()
            .statusCode(500);
    }

    @Test(dataProvider = "getGetUserObjectData")
    public void testGetUserObject(UserRequestData requestBody) {
        ResponseBody<UserResponseData> responseBody = given()
            .body(requestBody)
            .when()
            .post(USERS_API_URL)
            .then()
            .statusCode(201)
            .extract()
            .as(new TypeRef<>(){});

        Long id = responseBody.getData().getId();
        allCreatedUsers.add(id);
        ResponseBody<UserResponseData> expectedResponseBody = ResponseBody.<UserResponseData>builder()
            .meta(responseBody.getMeta())
            .data(responseBody.getData())
            .build();
        ResponseBody<UserResponseData> actualResponseBody = given()
            .pathParam("id", id)
            .when()
            .get(USER_BY_ID_API_URL)
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
        });
    }

    @Test
    public void testGetDeletedUserObject() {
        UserRequestData requestBody = UserRequestData.builder()
            .name(faker.name().fullName())
            .gender(Gender.FEMALE)
            .email(faker.internet().emailAddress())
            .status(Status.ACTIVE)
            .build();

        ResponseBody<UserResponseData> responseBody = given()
            .body(requestBody)
            .when()
            .post(USERS_API_URL)
            .then()
            .statusCode(201)
            .extract()
            .as(new TypeRef<>(){});

        Long id = responseBody.getData().getId();
        given()
            .pathParam("id", id)
            .when()
            .delete(USER_BY_ID_API_URL);

        ResponseBody<ErrorMessageResponseData> actualResponseBody = given()
            .pathParam("id", id)
            .when()
            .get(USER_BY_ID_API_URL)
            .then()
            .statusCode(404)
            .extract()
            .as(new TypeRef<>(){});

        ResponseBody<ErrorMessageResponseData> expectedResponseBody = ResponseBody.<ErrorMessageResponseData>builder()
            .meta(null)
            .data(new ErrorMessageResponseData("Resource not found"))
            .build();

        SoftAssertions.assertSoftly(softly -> softly.assertThat(actualResponseBody).isEqualTo(expectedResponseBody));
    }

    @Test
    public void testGetUserObjectInvalidInput() {
        ResponseBody<ErrorMessageResponseData> actualResponseBody = given()
            .when()
            .get(USER_BY_ID_INVALID_INPUT_API_URL)
            .then()
            .statusCode(404)
            .extract()
            .as(new TypeRef<>(){});

        ResponseBody<ErrorMessageResponseData> expectedResponseBody = ResponseBody.<ErrorMessageResponseData>builder()
            .meta(null)
            .data(new ErrorMessageResponseData("Resource not found"))
            .build();

        SoftAssertions.assertSoftly(softly -> softly.assertThat(actualResponseBody).isEqualTo(expectedResponseBody));
    }

    @Test
    public void testGetAllUserObjectsFirstPage() {
        for (int i = 0; i < 20; i++) {
            UserRequestData requestBody = UserRequestData.builder()
                .name(faker.name().fullName())
                .gender(Gender.FEMALE)
                .email(faker.internet().emailAddress())
                .status(Status.ACTIVE)
                .build();
            ResponseBody<UserResponseData> responseBody = given()
                .body(requestBody)
                .when()
                .post(USERS_API_URL)
                .then()
                .statusCode(201)
                .extract()
                .as(new TypeRef<>(){});
            allCreatedUsers.add(responseBody.getData().getId());
        }

        ResponseBody<List<UserResponseData>> actualResponseBody = given()
            .when()
            .get(USERS_API_URL)
            .then()
            .statusCode(200)
            .extract()
            .as(new TypeRef<>(){});
        List<Long> userIds = actualResponseBody.getData().stream()
            .map(UserResponseData::getId)
            .collect(Collectors.toList());

        ResponseBody<List<UserResponseData>> expectedResponseBody = ResponseBody.<List<UserResponseData>>builder()
            .meta(new Metadata(
                Pagination
                    .builder()
                    .page(1).limit(20)
                    .links(Links
                        .builder()
                        .current(String.format(BASE_URL + USERS_PAGE_API_URL, 1))
                        .next(String.format(BASE_URL + USERS_PAGE_API_URL, 2))
                        .build()
                    )
                    .build()
            ))
            .data(userIds.stream()
                    .map(id -> {
                        ResponseBody<UserResponseData> responseBody = given()
                            .pathParam("id", id)
                            .when()
                            .get(USER_BY_ID_API_URL)
                            .then()
                            .statusCode(200)
                            .extract()
                            .as(new TypeRef<>(){});
                        return responseBody.getData();
                    })
                        .collect(Collectors.toList())
            )
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
            softly.assertThat(actualResponseBody)
                  .extracting(r -> r.getMeta().getPagination().getLinks().getPrevious())
                  .isNull();
        });
    }

    @Test(dataProvider = "getPutUserObjectData")
    public void testPutUserObject(UserRequestData requestBody, ResponseBody<UserResponseData> expectedResponseBody) {
        ResponseBody<UserResponseData> postResponseBody = given()
            .body(UserRequestData.builder()
                .name(faker.name().fullName())
                .gender(Gender.MALE)
                .email(faker.internet().emailAddress())
                .status(Status.INACTIVE)
                .build()
            )
            .when()
            .post(USERS_API_URL)
            .then()
            .statusCode(201)
            .extract()
            .as(new TypeRef<>(){});
        Long id = postResponseBody.getData().getId();
        allCreatedUsers.add(id);

        ResponseBody<UserResponseData> actualResponseBody = given()
            .pathParam("id", id)
            .body(requestBody)
            .when()
            .put(USER_BY_ID_API_URL)
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
        });
    }

    @Test(dataProvider = "getPostOrPutUserObjectNegativeData")
    public void testPutUserObjectNegative(
        UserRequestData requestBody,
        ResponseBody<List<ErrorResponseData>> expectedResponseBody
    ) {
        ResponseBody<UserResponseData> postResponseBody = given()
            .body(UserRequestData.builder()
                .name(faker.name().fullName())
                .gender(Gender.MALE)
                .email(faker.internet().emailAddress())
                .status(Status.INACTIVE)
                .build()
            )
            .when()
            .post(USERS_API_URL)
            .then()
            .statusCode(201)
            .extract()
            .as(new TypeRef<>(){});
        Long id = postResponseBody.getData().getId();
        allCreatedUsers.add(id);

        ResponseBody<List<ErrorResponseData>> actualResponseBody = given()
            .pathParam("id", id)
            .body(requestBody)
            .when()
            .put(USER_BY_ID_API_URL)
            .then()
            .statusCode(422)
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

    @Test(dataProvider = "getPutUserObjectMissingFieldsData")
    public void testPutUserObjectMissingFields(
        UserRequestData postRequestBody,
        UserRequestData requestBody,
        ResponseBody<UserResponseData> expectedResponseBody
    ) {
        ResponseBody<UserResponseData> postResponseBody = given()
            .body(postRequestBody)
            .when()
            .post(USERS_API_URL)
            .then()
            .statusCode(201)
            .extract()
            .as(new TypeRef<>(){});
        Long id = postResponseBody.getData().getId();
        allCreatedUsers.add(id);

        ResponseBody<UserResponseData> actualResponseBody = given()
            .pathParam("id", id)
            .body(requestBody, new Jackson2Mapper(
                (type, s) -> new ObjectMapper().setSerializationInclusion(Include.NON_NULL)
            ))
            .when()
            .put(USER_BY_ID_API_URL)
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
        });
    }

    @Test
    public void testPutDeletedUserObject() {
        UserRequestData requestBody = UserRequestData.builder()
            .name(faker.name().fullName())
            .gender(Gender.FEMALE)
            .email(faker.internet().emailAddress())
            .status(Status.ACTIVE)
            .build();

        ResponseBody<UserResponseData> responseBody = given()
            .body(requestBody)
            .when()
            .post(USERS_API_URL)
            .then()
            .statusCode(201)
            .extract()
            .as(new TypeRef<>(){});

        Long id = responseBody.getData().getId();
        given()
            .pathParam("id", id)
            .when()
            .delete(USER_BY_ID_API_URL);

        ResponseBody<ErrorMessageResponseData> actualResponseBody = given()
            .pathParam("id", id)
            .body(requestBody)
            .when()
            .put(USER_BY_ID_API_URL)
            .then()
            .statusCode(404)
            .extract()
            .as(new TypeRef<>(){});

        ResponseBody<ErrorMessageResponseData> expectedResponseBody = ResponseBody.<ErrorMessageResponseData>builder()
             .meta(null)
             .data(new ErrorMessageResponseData("Resource not found"))
             .build();

        SoftAssertions.assertSoftly(softly -> softly.assertThat(actualResponseBody).isEqualTo(expectedResponseBody));
    }

    @Test
    public void testPutUserObjectEmailAlreadyExists() {
        Object[][] data = new Object[][] {
            {faker.name().fullName(), Gender.MALE, faker.internet().emailAddress(), Status.ACTIVE},
            {faker.name().fullName(), Gender.FEMALE, faker.internet().emailAddress(), Status.INACTIVE}
        };
        List<UserResponseData> responseBodies = Arrays
            .stream(data)
            .map(e -> {
                UserRequestData requestBody = UserRequestData.builder()
                    .name((String) e[0])
                    .gender((Gender) e[1])
                    .email((String) e[2])
                    .status((Status) e[3])
                    .build();
                ResponseBody<UserResponseData> responseBody = given()
                    .body(requestBody)
                    .when()
                    .post(USERS_API_URL)
                    .then()
                    .statusCode(201)
                    .extract()
                    .as(new TypeRef<>(){});
                Long id = responseBody.getData().getId();
                allCreatedUsers.add(id);
                return responseBody.getData();
            })
            .collect(Collectors.toList());

        ResponseBody<List<ErrorResponseData>> actualResponseBody = given()
            .pathParam("id", responseBodies.get(1).getId())
            .body(
                UserRequestData.builder()
                    .email(responseBodies.get(0).getEmail())
                    .build(),
                new Jackson2Mapper(
                    (type, s) -> new ObjectMapper().setSerializationInclusion(Include.NON_NULL)
                )
            )
            .when()
            .put(USER_BY_ID_API_URL)
            .then()
            .statusCode(422)
            .extract()
            .as(new TypeRef<>(){});
        ResponseBody<List<ErrorResponseData>> expectedResponseBody = ResponseBody.<List<ErrorResponseData>>builder()
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

    @Test
    public void testPutUserObjectInvalidRequestBody() {
        UserRequestData requestBody = UserRequestData.builder()
            .name(faker.name().fullName())
            .gender(Gender.FEMALE)
            .email(faker.internet().emailAddress())
            .status(Status.ACTIVE)
            .build();

        ResponseBody<UserResponseData> responseBody = given()
            .body(requestBody)
            .when()
            .post(USERS_API_URL)
            .then()
            .statusCode(201)
            .extract()
            .as(new TypeRef<>(){});

        Long id = responseBody.getData().getId();
        allCreatedUsers.add(id);

        given()
            .pathParam("id", id)
            .body("{}'")
            .when()
            .put(USER_BY_ID_API_URL)
            .then()
            .statusCode(500);
    }

    @Test
    public void testPutUserObjectInvalidInput() {
        ResponseBody<ErrorMessageResponseData> actualResponseBody = given()
            .body("{}")
            .when()
            .put(USER_BY_ID_INVALID_INPUT_API_URL)
            .then()
            .statusCode(404)
            .extract()
            .as(new TypeRef<>(){});

        ResponseBody<ErrorMessageResponseData> expectedResponseBody = ResponseBody.<ErrorMessageResponseData>builder()
            .meta(null)
            .data(new ErrorMessageResponseData("Resource not found"))
            .build();

        SoftAssertions.assertSoftly(softly -> softly.assertThat(actualResponseBody).isEqualTo(expectedResponseBody));
    }

    @Test
    public void testDeleteUserObject() {
        UserRequestData requestBody = UserRequestData.builder()
            .name(faker.name().fullName())
            .gender(Gender.FEMALE)
            .email(faker.internet().emailAddress())
            .status(Status.ACTIVE)
            .build();
        ResponseBody<UserResponseData> responseBody = given()
            .body(requestBody)
            .when()
            .post(USERS_API_URL)
            .then()
            .statusCode(201)
            .extract()
            .as(new TypeRef<>(){});

        Long id = responseBody.getData().getId();
        given()
            .pathParam("id", id)
            .when()
            .delete(USER_BY_ID_API_URL)
            .then()
            .statusCode(204);

        ResponseBody<ErrorMessageResponseData> actualGetResponseBody = given()
            .pathParam("id", id)
            .when()
            .get(USER_BY_ID_API_URL)
            .then()
            .statusCode(404)
            .extract()
            .as(new TypeRef<>(){});
        ResponseBody<ErrorMessageResponseData> expectedGetResponseBody
            = ResponseBody.<ErrorMessageResponseData>builder()
                          .meta(null)
                          .data(new ErrorMessageResponseData("Resource not found"))
                          .build();

        SoftAssertions.assertSoftly(
            softly -> softly.assertThat(actualGetResponseBody).isEqualTo(expectedGetResponseBody)
        );
    }

    @Test
    public void testDeleteDeletedUserObject() {
        UserRequestData requestBody = UserRequestData.builder()
            .name(faker.name().fullName())
            .gender(Gender.FEMALE)
            .email(faker.internet().emailAddress())
            .status(Status.ACTIVE)
            .build();
        ResponseBody<UserResponseData> responseBody = given()
            .body(requestBody)
            .when()
            .post(USERS_API_URL)
            .then()
            .statusCode(201)
            .extract()
            .as(new TypeRef<>(){});

        Long id = responseBody.getData().getId();
        given()
            .pathParam("id", id)
            .when()
            .delete(USER_BY_ID_API_URL);

        ResponseBody<ErrorMessageResponseData> actualDeleteResponseBody = given()
            .pathParam("id", id)
            .when()
            .delete(USER_BY_ID_API_URL)
            .then()
            .statusCode(404)
            .extract()
            .as(new TypeRef<>(){});
        ResponseBody<ErrorMessageResponseData> expectedDeleteResponseBody
            = ResponseBody.<ErrorMessageResponseData>builder()
                          .meta(null)
                          .data(new ErrorMessageResponseData("Resource not found"))
                          .build();

        SoftAssertions.assertSoftly(
            softly -> softly.assertThat(actualDeleteResponseBody).isEqualTo(expectedDeleteResponseBody)
        );
    }

    @Test
    public void testDeleteUserObjectInvalidInput() {
        ResponseBody<ErrorMessageResponseData> actualResponseBody = given()
            .when()
            .delete(USER_BY_ID_INVALID_INPUT_API_URL)
            .then()
            .statusCode(404)
            .extract()
            .as(new TypeRef<>(){});

        ResponseBody<ErrorMessageResponseData> expectedResponseBody = ResponseBody.<ErrorMessageResponseData>builder()
            .meta(null)
            .data(new ErrorMessageResponseData("Resource not found"))
            .build();

        SoftAssertions.assertSoftly(softly -> softly.assertThat(actualResponseBody).isEqualTo(expectedResponseBody));
    }

}
