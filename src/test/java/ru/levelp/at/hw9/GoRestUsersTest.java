package ru.levelp.at.hw9;

import static io.restassured.RestAssured.given;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveOAuth2HeaderScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.levelp.at.hw9.templates.Gender;
import ru.levelp.at.hw9.templates.Status;
import ru.levelp.at.hw9.templates.request.data.UserRequestData;
import ru.levelp.at.hw9.templates.response.ResponseBody;
import ru.levelp.at.hw9.templates.response.data.ErrorResponseData;
import ru.levelp.at.hw9.templates.response.data.UserResponseData;

public class GoRestUsersTest extends BaseTest {

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

}