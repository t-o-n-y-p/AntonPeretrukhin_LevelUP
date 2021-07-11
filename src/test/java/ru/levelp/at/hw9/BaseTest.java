package ru.levelp.at.hw9;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveOAuth2HeaderScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeSuite;
import ru.levelp.at.hw9.steps.ActionStep;
import ru.levelp.at.hw9.steps.AssertionStep;
import ru.levelp.at.hw9.templates.request.data.CommentRequestData;
import ru.levelp.at.hw9.templates.request.data.PostRequestData;
import ru.levelp.at.hw9.templates.request.data.UserRequestData;

public abstract class BaseTest {

    protected final List<Long> allCreatedUsers = new ArrayList<>();
    protected final List<Long> allCreatedPosts = new ArrayList<>();
    protected final List<Long> allCreatedComments = new ArrayList<>();
    protected final Faker faker = new Faker();

    protected final ActionStep actionStep = new ActionStep();
    protected final AssertionStep assertionStep = new AssertionStep();

    @BeforeSuite
    public void setUp() {
        PreemptiveOAuth2HeaderScheme authenticationScheme = new PreemptiveOAuth2HeaderScheme();
        authenticationScheme.setAccessToken("3ae4f0a21a4d9d7043dd096d767bce4d8567437c411c849cb0a180f27c40fed9");

        RestAssured.baseURI = RequestUriUtil.BASE_URL;
        RestAssured.requestSpecification = new RequestSpecBuilder()
            .log(LogDetail.ALL)
            .setContentType(ContentType.JSON)
            .setAuth(authenticationScheme)
            .build();

        RestAssured.responseSpecification = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .build();
    }

    @AfterClass
    public void tearDown() {
        for (Long id : allCreatedComments) {
            actionStep.deleteObject(id, CommentRequestData.class);
        }
        allCreatedComments.clear();
        for (Long id : allCreatedPosts) {
            actionStep.deleteObject(id, PostRequestData.class);
        }
        allCreatedPosts.clear();
        for (Long id : allCreatedUsers) {
            actionStep.deleteObject(id, UserRequestData.class);
        }
        allCreatedUsers.clear();
    }

}
