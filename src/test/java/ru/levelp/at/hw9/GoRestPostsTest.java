package ru.levelp.at.hw9;

import static io.restassured.RestAssured.given;

import io.restassured.common.mapper.TypeRef;
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

    // ------------------------------
    // TESTS
    // ------------------------------

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

}
