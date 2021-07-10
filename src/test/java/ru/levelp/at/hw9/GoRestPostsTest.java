package ru.levelp.at.hw9;

import static io.restassured.RestAssured.given;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.common.mapper.TypeRef;
import io.restassured.internal.mapping.Jackson2Mapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.levelp.at.hw9.templates.Gender;
import ru.levelp.at.hw9.templates.Status;
import ru.levelp.at.hw9.templates.request.data.PostRequestData;
import ru.levelp.at.hw9.templates.request.data.UserRequestData;
import ru.levelp.at.hw9.templates.response.ResponseBody;
import ru.levelp.at.hw9.templates.response.data.ErrorMessageResponseData;
import ru.levelp.at.hw9.templates.response.data.ErrorResponseData;
import ru.levelp.at.hw9.templates.response.data.PostResponseData;
import ru.levelp.at.hw9.templates.response.data.UserResponseData;

public class GoRestPostsTest extends BaseTest {

    private List<Long> userIds;

    @BeforeClass
    public void beforeClass() {
        Object[][] userData = new Object[][] {
            {faker.name().fullName(), Gender.MALE, faker.internet().emailAddress(), Status.ACTIVE},
            {faker.name().fullName(), Gender.FEMALE, faker.internet().emailAddress(), Status.INACTIVE}
        };
        userIds = Arrays.stream(userData)
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
                return id;
            })
            .collect(Collectors.toList());
    }

    @DataProvider
    private Object[] getGetPostObjectData() {
        Object[][] postData = new Object[][] {
            {userIds.get(0), faker.animal().name(), faker.yoda().quote()},
            {userIds.get(1), faker.animal().name(), faker.yoda().quote()}
        };
        return Arrays.stream(postData)
                     .map(e -> PostRequestData
                         .builder()
                         .userId((Long) e[0])
                         .title((String) e[1])
                         .body((String) e[2])
                         .build()
                     ).toArray();
    }

    @DataProvider
    private Object[][] getPostPostObjectData() {
        return getPostObjectData();
    }

    @DataProvider
    private Object[][] getPutPostObjectData() {
        return getPostObjectData();
    }

    private Object[][] getPostObjectData() {
        Object[][] data = new Object[][] {
            {userIds.get(0), faker.animal().name(), faker.yoda().quote()},
            {userIds.get(1), faker.animal().name(), faker.yoda().quote()}
        };
        return Arrays.stream(data)
                     .map(e -> new Object[]{
                         PostRequestData.builder()
                             .userId((Long) e[0])
                             .title((String) e[1])
                             .body((String) e[2])
                             .build(),
                         ResponseBody.<PostResponseData>builder()
                             .meta(null)
                             .data(
                                 PostResponseData.builder()
                                     .userId((Long) e[0])
                                     .title((String) e[1])
                                     .body((String) e[2])
                                     .build()
                             )
                             .build()
                     })
                     .toArray(Object[][]::new);
    }

    @DataProvider
    private Object[][] getPostOrPutPostObjectNegativeData() {
        return new Object[][]{
            {
                PostRequestData.builder()
                    .userId(userIds.get(0))
                    .title("")
                    .body("    ")
                    .build(),
                ResponseBody.<List<ErrorResponseData>>builder()
                    .meta(null)
                    .data(List.of(
                        new ErrorResponseData("title", "can't be blank"),
                        new ErrorResponseData("body", "can't be blank")
                    ))
                    .build()
            },
            {
                PostRequestData.builder()
                    .userId(null)
                    .title("    ")
                    .body("0".repeat(10000))
                    .build(),
                ResponseBody.<List<ErrorResponseData>>builder()
                    .meta(null)
                    .data(List.of(
                        new ErrorResponseData("user_id", "can't be blank"),
                        new ErrorResponseData("title", "can't be blank"),
                        new ErrorResponseData("body", "is too long (maximum is 1000 characters)")
                    ))
                    .build()
            },
            {
                PostRequestData.builder()
                    .userId(userIds.get(0))
                    .title("0".repeat(10000))
                    .body(null)
                    .build(),
                ResponseBody.<List<ErrorResponseData>>builder()
                    .meta(null)
                    .data(List.of(
                        new ErrorResponseData("title", "is too long (maximum is 200 characters)"),
                        new ErrorResponseData("body", "can't be blank")
                    ))
                    .build()
            },
            {
                PostRequestData.builder()
                    .userId(null)
                    .title(null)
                    .body("")
                    .build(),
                ResponseBody.<List<ErrorResponseData>>builder()
                    .meta(null)
                    .data(List.of(
                        new ErrorResponseData("user_id", "can't be blank"),
                        new ErrorResponseData("title", "can't be blank"),
                        new ErrorResponseData("body", "can't be blank")
                    ))
                    .build()
            }
        };
    }

    @DataProvider
    private Object[][] getPostPostObjectMissingFieldsData() {
        return new Object[][]{
            {
                PostRequestData.builder()
                    .title(faker.animal().name())
                    .body(faker.yoda().quote())
                    .build(),
                ResponseBody.<List<ErrorResponseData>>builder()
                    .meta(null)
                    .data(List.of(
                        new ErrorResponseData("user_id", "can't be blank")
                    ))
                    .build()
            },
            {
                PostRequestData.builder()
                    .userId(userIds.get(0))
                    .body(faker.yoda().quote())
                    .build(),
                ResponseBody.<List<ErrorResponseData>>builder()
                    .meta(null)
                    .data(List.of(
                        new ErrorResponseData("title", "can't be blank")
                    ))
                    .build()
            },
            {
                PostRequestData.builder()
                    .userId(userIds.get(0))
                    .title(faker.animal().name())
                    .build(),
                ResponseBody.<List<ErrorResponseData>>builder()
                    .meta(null)
                    .data(List.of(
                        new ErrorResponseData("body", "can't be blank")
                    ))
                    .build()
            }
        };
    }

    // ------------------------------
    // TESTS
    // ------------------------------

    @Test(dataProvider = "getPostPostObjectData")
    public void testPostPostObject(PostRequestData requestBody, ResponseBody<PostResponseData> expectedResponseBody) {
        ResponseBody<PostResponseData> actualResponseBody = given()
            .body(requestBody)
            .when()
            .post(POSTS_API_URL)
            .then()
            .statusCode(201)
            .extract()
            .as(new TypeRef<>(){});

        allCreatedPosts.add(actualResponseBody.getData().getId());

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

    @Test(dataProvider = "getPostOrPutPostObjectNegativeData")
    public void testPostPostObjectNegative(
        PostRequestData requestBody,
        ResponseBody<List<ErrorResponseData>> expectedResponseBody
    ) {
        ResponseBody<List<ErrorResponseData>> actualResponseBody = given()
            .body(requestBody)
            .when()
            .post(POSTS_API_URL)
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
    public void testPostPostObjectDeletedUser() {
        UserRequestData userRequestBody = UserRequestData.builder()
            .name(faker.name().fullName())
            .gender(Gender.FEMALE)
            .email(faker.internet().emailAddress())
            .status(Status.ACTIVE)
            .build();
        ResponseBody<UserResponseData> userResponseBody = given()
            .body(userRequestBody)
            .when()
            .post(USERS_API_URL)
            .then()
            .statusCode(201)
            .extract()
            .as(new TypeRef<>(){});
        Long userId = userResponseBody.getData().getId();
        allCreatedUsers.add(userId);

        given()
            .pathParam("id", userId)
            .when()
            .delete(USER_BY_ID_API_URL);

        PostRequestData requestBody = PostRequestData
            .builder()
            .userId(userId)
            .title(faker.animal().name())
            .body(faker.yoda().quote())
            .build();
        ResponseBody<List<ErrorResponseData>> actualResponseBody = given()
            .body(requestBody)
            .when()
            .post(POSTS_API_URL)
            .then()
            .statusCode(422)
            .extract()
            .as(new TypeRef<>(){});
        ResponseBody<List<ErrorResponseData>> expectedResponseBody = ResponseBody.<List<ErrorResponseData>>builder()
            .meta(null)
            .data(List.of(
                new ErrorResponseData("user", "must exist")
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

    @Test(dataProvider = "getPostPostObjectMissingFieldsData")
    public void testPostPostObjectMissingFields(
        PostRequestData requestBody,
        ResponseBody<List<ErrorResponseData>> expectedResponseBody
    ) {
        ResponseBody<List<ErrorResponseData>> actualResponseBody = given()
            .body(requestBody, new Jackson2Mapper(
                (type, s) -> new ObjectMapper().setSerializationInclusion(Include.NON_NULL)
            ))
            .when()
            .post(POSTS_API_URL)
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
    public void testPostPostObjectInvalidRequestBody() {
        given()
            .body("{}'")
            .when()
            .post(POSTS_API_URL)
            .then()
            .statusCode(500);
    }

    @Test(dataProvider = "getGetPostObjectData")
    public void testGetPostObject(PostRequestData requestBody) {
        ResponseBody<PostResponseData> responseBody = given()
            .body(requestBody)
            .when()
            .post(POSTS_API_URL)
            .then()
            .statusCode(201)
            .extract()
            .as(new TypeRef<>(){});

        Long id = responseBody.getData().getId();
        allCreatedPosts.add(id);
        ResponseBody<PostResponseData> expectedResponseBody = ResponseBody.<PostResponseData>builder()
            .meta(responseBody.getMeta())
            .data(responseBody.getData())
            .build();
        ResponseBody<PostResponseData> actualResponseBody = given()
            .pathParam("id", id)
            .when()
            .get(POST_BY_ID_API_URL)
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
    public void testGetDeletedPostObject() {
        PostRequestData requestBody = PostRequestData.builder()
            .userId(userIds.get(0))
            .title(faker.animal().name())
            .body(faker.yoda().quote())
            .build();
        ResponseBody<PostResponseData> responseBody = given()
            .body(requestBody)
            .when()
            .post(POSTS_API_URL)
            .then()
            .statusCode(201)
            .extract()
            .as(new TypeRef<>(){});

        Long id = responseBody.getData().getId();
        allCreatedPosts.add(id);

        given()
            .pathParam("id", id)
            .when()
            .delete(POST_BY_ID_API_URL);

        ResponseBody<ErrorMessageResponseData> actualResponseBody = given()
            .pathParam("id", id)
            .when()
            .get(POST_BY_ID_API_URL)
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
    public void testGetPostObjectInvalidInput() {
        ResponseBody<ErrorMessageResponseData> actualResponseBody = given()
            .when()
            .get(POST_BY_ID_INVALID_INPUT_API_URL)
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
    public void testDeletePostObject() {
        PostRequestData requestBody = PostRequestData.builder()
            .userId(userIds.get(0))
            .title(faker.animal().name())
            .body(faker.yoda().quote())
            .build();
        ResponseBody<PostResponseData> responseBody = given()
            .body(requestBody)
            .when()
            .post(POSTS_API_URL)
            .then()
            .statusCode(201)
            .extract()
            .as(new TypeRef<>(){});

        Long id = responseBody.getData().getId();
        given()
            .pathParam("id", id)
            .when()
            .delete(POST_BY_ID_API_URL)
            .then()
            .statusCode(204);

        ResponseBody<ErrorMessageResponseData> actualGetResponseBody = given()
            .pathParam("id", id)
            .when()
            .get(POST_BY_ID_API_URL)
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
    public void testDeleteDeletedPostObject() {
        PostRequestData requestBody = PostRequestData.builder()
            .userId(userIds.get(0))
            .title(faker.animal().name())
            .body(faker.yoda().quote())
            .build();
        ResponseBody<PostResponseData> responseBody = given()
            .body(requestBody)
            .when()
            .post(POSTS_API_URL)
            .then()
            .statusCode(201)
            .extract()
            .as(new TypeRef<>(){});

        Long id = responseBody.getData().getId();
        given()
            .pathParam("id", id)
            .when()
            .delete(POST_BY_ID_API_URL);

        ResponseBody<ErrorMessageResponseData> actualDeleteResponseBody = given()
            .pathParam("id", id)
            .when()
            .delete(POST_BY_ID_API_URL)
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
    public void testDeletePostObjectInvalidInput() {
        ResponseBody<ErrorMessageResponseData> actualResponseBody = given()
            .when()
            .delete(POST_BY_ID_INVALID_INPUT_API_URL)
            .then()
            .statusCode(404)
            .extract()
            .as(new TypeRef<>(){});

        ResponseBody<ErrorMessageResponseData> expectedResponseBody
            = ResponseBody.<ErrorMessageResponseData>builder()
                          .meta(null)
                          .data(new ErrorMessageResponseData("Resource not found"))
                          .build();

        SoftAssertions.assertSoftly(softly -> softly.assertThat(actualResponseBody).isEqualTo(expectedResponseBody));
    }

}
