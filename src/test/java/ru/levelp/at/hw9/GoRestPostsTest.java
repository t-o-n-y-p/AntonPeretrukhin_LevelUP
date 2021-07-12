package ru.levelp.at.hw9;

import static io.restassured.RestAssured.given;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.common.mapper.TypeRef;
import io.restassured.internal.mapping.Jackson2Mapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.levelp.at.hw9.templates.Gender;
import ru.levelp.at.hw9.templates.Status;
import ru.levelp.at.hw9.templates.request.data.PostRequestData;
import ru.levelp.at.hw9.templates.request.data.UserRequestData;
import ru.levelp.at.hw9.templates.response.Links;
import ru.levelp.at.hw9.templates.response.Metadata;
import ru.levelp.at.hw9.templates.response.Pagination;
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
                ResponseBody<UserResponseData> responseBody = actionStep.postObject(
                    UserRequestData.builder()
                        .name((String) e[0])
                        .gender((Gender) e[1])
                        .email((String) e[2])
                        .status((Status) e[3])
                        .build(),
                    201
                ).as(new TypeRef<>(){});
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
            .map(e -> PostRequestData.builder()
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
                    .userId(userIds.get(1))
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
                    .userId(userIds.get(1))
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

    @DataProvider
    private Object[][] getPutPostObjectMissingFieldsData() {
        String title = faker.animal().name();
        String body = faker.yoda().quote();
        Object[][] postData = new Object[][] {
            {userIds.get(0), faker.animal().name(), body},
            {userIds.get(1), faker.animal().name(), faker.yoda().quote()},
            {userIds.get(0), title, faker.yoda().quote()}
        };
        Object[][] putData = new Object[][] {
            {null, faker.animal().name(), body},
            {userIds.get(1), null, faker.yoda().quote()},
            {userIds.get(1), title, null}
        };
        return IntStream.range(0, Integer.min(postData.length, putData.length))
            .mapToObj(i -> new Object[]{
                PostRequestData.builder()
                    .userId((Long) postData[i][0])
                    .title((String) postData[i][1])
                    .body((String) postData[i][2])
                    .build(),
                PostRequestData.builder()
                    .userId((Long) putData[i][0])
                    .title((String) putData[i][1])
                    .body((String) putData[i][2])
                    .build(),
                ResponseBody.<PostResponseData>builder()
                    .meta(null)
                    .data(
                        PostResponseData.builder()
                            .userId((Long) (putData[i][0] == null ? postData[i][0] : putData[i][0]))
                            .title((String) (putData[i][1] == null ? postData[i][1] : putData[i][1]))
                            .body((String) (putData[i][2] == null ? postData[i][2] : putData[i][2]))
                            .build()
                    )
                    .build()
            })
            .toArray(Object[][]::new);

    }

    // ------------------------------
    // TESTS
    // ------------------------------

    @Test(dataProvider = "getPostPostObjectData")
    public void testPostPostObject(PostRequestData requestBody, ResponseBody<PostResponseData> expectedResponseBody) {
        ResponseBody<PostResponseData> actualResponseBody
            = actionStep.postObject(requestBody, 201).as(new TypeRef<>(){});
        allCreatedPosts.add(actualResponseBody.getData().getId());
        assertionStep.assertObjectResponseBody(actualResponseBody, expectedResponseBody);
    }

    @Test(dataProvider = "getPostOrPutPostObjectNegativeData")
    public void testPostPostObjectNegative(
        PostRequestData requestBody,
        ResponseBody<List<ErrorResponseData>> expectedResponseBody
    ) {
        ResponseBody<List<ErrorResponseData>> actualResponseBody
            = actionStep.postObject(requestBody, 422).as(new TypeRef<>(){});
        assertionStep.assertObjectResponseBodyWithErrors(actualResponseBody, expectedResponseBody);
    }

    @Test
    public void testPostPostObjectDeletedUser() {
        ResponseBody<UserResponseData> userResponseBody = actionStep.postObject(
            UserRequestData.builder()
                .name(faker.name().fullName())
                .gender(Gender.FEMALE)
                .email(faker.internet().emailAddress())
                .status(Status.ACTIVE)
                .build(),
            201
        ).as(new TypeRef<>(){});
        Long userId = userResponseBody.getData().getId();
        actionStep.deleteObject(userId, UserRequestData.class, 204);

        ResponseBody<List<ErrorResponseData>> actualResponseBody = actionStep.postObject(
            PostRequestData.builder()
                .userId(userId)
                .title(faker.animal().name())
                .body(faker.yoda().quote())
                .build(),
            422
        ).as(new TypeRef<>(){});
        ResponseBody<List<ErrorResponseData>> expectedResponseBody = ResponseBody.<List<ErrorResponseData>>builder()
            .meta(null)
            .data(List.of(
                new ErrorResponseData("user", "must exist")
            ))
            .build();
        assertionStep.assertObjectResponseBodyWithErrors(actualResponseBody, expectedResponseBody);
    }

    @Test(dataProvider = "getPostPostObjectMissingFieldsData")
    public void testPostPostObjectMissingFields(
        PostRequestData requestBody,
        ResponseBody<List<ErrorResponseData>> expectedResponseBody
    ) {
        ResponseBody<List<ErrorResponseData>> actualResponseBody = actionStep.postObject(
            requestBody, 422,
            new Jackson2Mapper(
                (type, s) -> new ObjectMapper().setSerializationInclusion(Include.NON_NULL)
            )
        ).as(new TypeRef<>(){});
        assertionStep.assertObjectResponseBodyWithErrors(actualResponseBody, expectedResponseBody);
    }

    @Test
    public void testPostPostObjectInvalidRequestBody() {
        actionStep.postObjectInvalidRequestBody(PostRequestData.class);
    }

    @Test(dataProvider = "getGetPostObjectData")
    public void testGetPostObject(PostRequestData requestBody) {
        ResponseBody<PostResponseData> responseBody = actionStep.postObject(requestBody, 201).as(new TypeRef<>(){});
        Long id = responseBody.getData().getId();
        allCreatedPosts.add(id);
        ResponseBody<PostResponseData> expectedResponseBody = ResponseBody.<PostResponseData>builder()
            .meta(responseBody.getMeta())
            .data(responseBody.getData())
            .build();
        ResponseBody<PostResponseData> actualResponseBody
            = actionStep.getObject(id, PostRequestData.class, 200).as(new TypeRef<>(){});
        assertionStep.assertObjectResponseBody(actualResponseBody, expectedResponseBody);
    }

    @Test
    public void testGetDeletedPostObject() {
        ResponseBody<PostResponseData> responseBody = actionStep.postObject(
            PostRequestData.builder()
                .userId(userIds.get(0))
                .title(faker.animal().name())
                .body(faker.yoda().quote())
                .build(),
            201
        ).as(new TypeRef<>(){});
        Long id = responseBody.getData().getId();
        actionStep.deleteObject(id, PostRequestData.class, 204);

        ResponseBody<ErrorMessageResponseData> actualResponseBody
            = actionStep.getObject(id, PostRequestData.class, 404).as(new TypeRef<>(){});
        assertionStep.assertResourceNotFoundError(actualResponseBody);
    }

    @Test
    public void testGetPostObjectInvalidInput() {
        ResponseBody<ErrorMessageResponseData> actualResponseBody
            = actionStep.getObjectWithInvalidInput(PostRequestData.class).as(new TypeRef<>(){});
        assertionStep.assertResourceNotFoundError(actualResponseBody);
    }

    @Test
    public void testGetAllPostObjectsFirstPage() {
        for (int i = 0; i < 20; i++) {
            ResponseBody<PostResponseData> responseBody = actionStep.postObject(
                PostRequestData.builder()
                    .userId(userIds.get(i % 2))
                    .title(faker.animal().name())
                    .body(faker.yoda().quote())
                    .build(),
                201
            ).as(new TypeRef<>(){});
            allCreatedPosts.add(responseBody.getData().getId());
        }

        ResponseBody<List<PostResponseData>> actualResponseBody
            = actionStep.getObjectsFirstPage(PostRequestData.class).as(new TypeRef<>(){});
        ResponseBody<List<PostResponseData>> expectedResponseBody;
        if (actualResponseBody.getData() == null || actualResponseBody.getData().isEmpty()) {
            expectedResponseBody = ResponseBody.<List<PostResponseData>>builder()
                .meta(new Metadata(
                    Pagination
                        .builder()
                        .page(1).limit(20)
                        .links(Links
                            .builder()
                            .current(RequestUriUtil.getPageUri(1, PostRequestData.class))
                            .build()
                        )
                         .build()
                ))
                .data(Collections.emptyList())
                .build();
        } else {
            List<Long> postIds = actualResponseBody.getData().stream()
                .map(PostResponseData::getId)
                .collect(Collectors.toList());

            expectedResponseBody = ResponseBody.<List<PostResponseData>>builder()
                .meta(new Metadata(
                    Pagination
                        .builder()
                        .page(1).limit(20)
                        .links(Links
                            .builder()
                            .current(RequestUriUtil.getPageUri(1, PostRequestData.class))
                            .build()
                        )
                        .build()
                ))
                .data(postIds.stream()
                    .map(id -> {
                        ResponseBody<PostResponseData> responseBody = given()
                            .pathParam("id", id)
                            .when()
                            .get(RequestUriUtil.OBJECT_BY_ID_API_URL.get(PostRequestData.class))
                            .then()
                            .statusCode(200)
                            .extract()
                            .as(new TypeRef<>(){});
                        return responseBody.getData();
                    })
                    .collect(Collectors.toList())
                )
                .build();
        }
        assertionStep.assertObjectsFirstPageResponse(
            actualResponseBody, expectedResponseBody, RequestUriUtil.getPageUri(2, PostRequestData.class)
        );
    }

    @Test(dataProvider = "getPutPostObjectData")
    public void testPutPostObject(PostRequestData requestBody, ResponseBody<PostResponseData> expectedResponseBody) {
        ResponseBody<PostResponseData> postResponseBody = actionStep.postObject(
            PostRequestData.builder()
                .userId(userIds.get(0))
                .title(faker.animal().name())
                .body(faker.yoda().quote())
                .build(),
            201
        ).as(new TypeRef<>(){});
        Long id = postResponseBody.getData().getId();
        allCreatedPosts.add(id);

        ResponseBody<PostResponseData> actualResponseBody
            = actionStep.putObject(id, requestBody, 200).as(new TypeRef<>(){});
        assertionStep.assertObjectResponseBody(actualResponseBody, expectedResponseBody);
    }

    @Test(dataProvider = "getPostOrPutPostObjectNegativeData")
    public void testPutPostObjectNegative(
        PostRequestData requestBody,
        ResponseBody<List<ErrorResponseData>> expectedResponseBody
    ) {
        ResponseBody<PostResponseData> postResponseBody = actionStep.postObject(
            PostRequestData.builder()
                .userId(userIds.get(0))
                .title(faker.animal().name())
                .body(faker.yoda().quote())
                .build(),
            201
        ).as(new TypeRef<>(){});
        Long id = postResponseBody.getData().getId();
        allCreatedPosts.add(id);

        ResponseBody<List<ErrorResponseData>> actualResponseBody
            = actionStep.putObject(id, requestBody, 422).as(new TypeRef<>(){});
        assertionStep.assertObjectResponseBodyWithErrors(actualResponseBody, expectedResponseBody);
    }

    @Test(dataProvider = "getPutPostObjectMissingFieldsData")
    public void testPutPostObjectMissingFields(
        PostRequestData postRequestBody,
        PostRequestData requestBody,
        ResponseBody<PostResponseData> expectedResponseBody
    ) {
        ResponseBody<PostResponseData> postResponseBody
            = actionStep.postObject(postRequestBody, 201).as(new TypeRef<>(){});
        Long id = postResponseBody.getData().getId();
        allCreatedPosts.add(id);

        ResponseBody<PostResponseData> actualResponseBody = actionStep.putObject(
            id, requestBody, 200,
            new Jackson2Mapper(
                (type, s) -> new ObjectMapper().setSerializationInclusion(Include.NON_NULL)
            )
        ).as(new TypeRef<>(){});
        assertionStep.assertObjectResponseBody(actualResponseBody, expectedResponseBody);
    }

    @Test
    public void testPutDeletedPostObject() {
        ResponseBody<PostResponseData> responseBody = actionStep.postObject(
            PostRequestData.builder()
                .userId(userIds.get(0))
                .title(faker.animal().name())
                .body(faker.yoda().quote())
                .build(),
            201
        ).as(new TypeRef<>(){});
        Long id = responseBody.getData().getId();
        actionStep.deleteObject(id, PostRequestData.class, 204);

        ResponseBody<ErrorMessageResponseData> actualResponseBody = actionStep.putObject(
            id,
            PostRequestData.builder()
                .userId(userIds.get(0))
                .title(faker.animal().name())
                .body(faker.yoda().quote())
                .build(),
            404
        ).as(new TypeRef<>(){});
        assertionStep.assertResourceNotFoundError(actualResponseBody);
    }

    @Test
    public void testPutPostObjectToDeletedUser() {
        ResponseBody<UserResponseData> postResponseBody = actionStep.postObject(
            UserRequestData.builder()
                .name(faker.name().fullName())
                .gender(Gender.FEMALE)
                .email(faker.internet().emailAddress())
                .status(Status.ACTIVE)
                .build(),
            201
        ).as(new TypeRef<>(){});
        Long userId = postResponseBody.getData().getId();

        ResponseBody<PostResponseData> responseBody = actionStep.postObject(
            PostRequestData.builder()
                .userId(userIds.get(0))
                .title(faker.animal().name())
                .body(faker.yoda().quote())
                .build(),
            201
        ).as(new TypeRef<>(){});
        Long id = responseBody.getData().getId();
        allCreatedPosts.add(id);

        actionStep.deleteObject(userId, UserRequestData.class, 204);

        ResponseBody<List<ErrorResponseData>> actualResponseBody = actionStep.putObject(
            id,
            PostRequestData.builder()
                .userId(userId)
                .build(),
            422,
            new Jackson2Mapper(
                (type, s) -> new ObjectMapper().setSerializationInclusion(Include.NON_NULL)
            )
        ).as(new TypeRef<>(){});
        ResponseBody<List<ErrorResponseData>> expectedResponseBody = ResponseBody.<List<ErrorResponseData>>builder()
             .meta(null)
             .data(List.of(
                 new ErrorResponseData("user", "must exist")
             ))
             .build();
        assertionStep.assertObjectResponseBodyWithErrors(actualResponseBody, expectedResponseBody);
    }

    @Test
    public void testPutPostObjectInvalidRequestBody() {
        ResponseBody<PostResponseData> postResponseBody = actionStep.postObject(
            PostRequestData.builder()
                .userId(userIds.get(0))
                .title(faker.animal().name())
                .body(faker.yoda().quote())
                .build(),
            201
        ).as(new TypeRef<>(){});
        Long id = postResponseBody.getData().getId();
        allCreatedPosts.add(id);

        actionStep.putObjectInvalidRequestBody(id, PostRequestData.class);
    }

    @Test
    public void testPutPostObjectInvalidInput() {
        ResponseBody<ErrorMessageResponseData> actualResponseBody
            = actionStep.putObjectWithInvalidInput(PostRequestData.class).as(new TypeRef<>(){});
        assertionStep.assertResourceNotFoundError(actualResponseBody);
    }

    @Test
    public void testDeletePostObject() {
        ResponseBody<PostResponseData> responseBody = actionStep.postObject(
            PostRequestData.builder()
                .userId(userIds.get(0))
                .title(faker.animal().name())
                .body(faker.yoda().quote())
                .build(),
            201
        ).as(new TypeRef<>(){});
        Long id = responseBody.getData().getId();
        actionStep.deleteObject(id, PostRequestData.class, 204);

        ResponseBody<ErrorMessageResponseData> actualGetResponseBody
            = actionStep.getObject(id, PostRequestData.class, 404).as(new TypeRef<>(){});
        assertionStep.assertResourceNotFoundError(actualGetResponseBody);
    }

    @Test
    public void testDeleteDeletedPostObject() {
        ResponseBody<PostResponseData> responseBody = actionStep.postObject(
            PostRequestData.builder()
                .userId(userIds.get(0))
                .title(faker.animal().name())
                .body(faker.yoda().quote())
                .build(),
            201
        ).as(new TypeRef<>(){});
        Long id = responseBody.getData().getId();
        actionStep.deleteObject(id, PostRequestData.class, 204);

        ResponseBody<ErrorMessageResponseData> actualDeleteResponseBody
            = actionStep.deleteObject(id, PostRequestData.class, 404).as(new TypeRef<>(){});
        assertionStep.assertResourceNotFoundError(actualDeleteResponseBody);
    }

    @Test
    public void testDeletePostObjectInvalidInput() {
        ResponseBody<ErrorMessageResponseData> actualResponseBody
            = actionStep.deleteObjectWithInvalidInput(PostRequestData.class).as(new TypeRef<>(){});
        assertionStep.assertResourceNotFoundError(actualResponseBody);
    }

}
