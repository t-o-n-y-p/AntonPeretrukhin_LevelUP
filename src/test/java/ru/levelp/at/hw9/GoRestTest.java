package ru.levelp.at.hw9;

import io.restassured.RestAssured;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.authentication.PreemptiveOAuth2HeaderScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.internal.http.HTTPBuilder;
import org.testng.annotations.BeforeSuite;

public class GoRestTest {

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



}
