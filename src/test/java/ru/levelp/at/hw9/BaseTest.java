package ru.levelp.at.hw9;

import static io.restassured.RestAssured.given;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveOAuth2HeaderScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public abstract class BaseTest {

    protected final List<Long> allCreatedUsers = new ArrayList<>();
    protected final List<Long> allCreatedPosts = new ArrayList<>();
    protected final List<Long> allCreatedComments = new ArrayList<>();
    protected final Faker faker = new Faker();

    @BeforeSuite
    public void setUp() {
        PreemptiveOAuth2HeaderScheme authenticationScheme = new PreemptiveOAuth2HeaderScheme();
        authenticationScheme.setAccessToken("3ae4f0a21a4d9d7043dd096d767bce4d8567437c411c849cb0a180f27c40fed9");

        RestAssured.baseURI = "https://gorest.co.in";
        RestAssured.requestSpecification = new RequestSpecBuilder()
            .log(LogDetail.ALL)
            .setContentType(ContentType.JSON)
            .setAuth(authenticationScheme)
            .build();

        RestAssured.responseSpecification = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .build();
    }

    @AfterSuite
    public void tearDown() {
        for (Long id : allCreatedUsers) {
            given()
                .pathParam("id", id)
                .when()
                .delete("/public-api/users/{id}");
        }
        for (Long id : allCreatedPosts) {
            given()
                .pathParam("id", id)
                .when()
                .delete("/public-api/posts/{id}");
        }
        for (Long id : allCreatedComments) {
            given()
                .pathParam("id", id)
                .when()
                .delete("/public-api/comments/{id}");
        }
    }

}
