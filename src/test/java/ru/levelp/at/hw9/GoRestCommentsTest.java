package ru.levelp.at.hw9;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.levelp.at.hw9.templates.Gender;
import ru.levelp.at.hw9.templates.Status;
import ru.levelp.at.hw9.templates.request.data.CommentRequestData;
import ru.levelp.at.hw9.templates.request.data.PostRequestData;
import ru.levelp.at.hw9.templates.request.data.UserRequestData;
import ru.levelp.at.hw9.templates.response.ResponseBody;
import ru.levelp.at.hw9.templates.response.data.CommentResponseData;
import ru.levelp.at.hw9.templates.response.data.ErrorMessageResponseData;
import ru.levelp.at.hw9.templates.response.data.ErrorResponseData;
import ru.levelp.at.hw9.templates.response.data.PostResponseData;
import ru.levelp.at.hw9.templates.response.data.UserResponseData;

public class GoRestCommentsTest extends BaseTest {

    private List<Long> userIds;
    private List<Long> postIds;

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
                        .build()
                );
                Long id = responseBody.getData().getId();
                allCreatedUsers.add(id);
                return id;
            })
            .collect(Collectors.toList());
        postIds = userIds.stream()
            .map(userId -> {
                ResponseBody<PostResponseData> responseBody = actionStep.postObject(
                    PostRequestData.builder()
                        .userId(userId)
                        .title(faker.animal().name())
                        .body(faker.yoda().quote())
                        .build()
                );
                Long id = responseBody.getData().getId();
                allCreatedPosts.add(id);
                return id;
            })
            .collect(Collectors.toList());
    }

    @DataProvider
    private Object[] getGetCommentObjectData() {
        Object[][] postData = new Object[][] {
            {postIds.get(0), faker.name().fullName(), faker.internet().emailAddress(), faker.yoda().quote()},
            {postIds.get(1), faker.name().fullName(), faker.internet().emailAddress(), faker.yoda().quote()}
        };
        return Arrays.stream(postData)
            .map(e -> CommentRequestData.builder()
                .postId((Long) e[0])
                .name((String) e[1])
                .email((String) e[2])
                .body((String) e[3])
                .build()
            ).toArray();
    }

    @DataProvider
    private Object[][] getPostCommentObjectData() {
        return getCommentObjectData();
    }

    @DataProvider
    private Object[][] getPutCommentObjectData() {
        return getCommentObjectData();
    }

    private Object[][] getCommentObjectData() {
        Object[][] data = new Object[][] {
            {postIds.get(0), faker.name().fullName(), faker.internet().emailAddress(), faker.yoda().quote()},
            {postIds.get(1), faker.name().fullName(), faker.internet().emailAddress(), faker.yoda().quote()}
        };
        return Arrays.stream(data)
            .map(e -> new Object[]{
                CommentRequestData.builder()
                    .postId((Long) e[0])
                    .name((String) e[1])
                    .email((String) e[2])
                    .body((String) e[3])
                    .build(),
                ResponseBody.<CommentResponseData>builder()
                    .meta(null)
                    .data(
                        CommentResponseData.builder()
                            .postId((Long) e[0])
                            .name((String) e[1])
                            .email((String) e[2])
                            .body((String) e[3])
                            .build()
                    )
                    .build()
            })
            .toArray(Object[][]::new);
    }

    @DataProvider
    private Object[][] getPostOrPutCommentObjectNegativeData() {
        return new Object[][]{
            {
                CommentRequestData.builder()
                    .postId(postIds.get(0))
                    .name(null)
                    .email("qwerty")
                    .body("    ")
                    .build(),
                ResponseBody.<List<ErrorResponseData>>builder()
                    .meta(null)
                    .data(List.of(
                        new ErrorResponseData("name", "can't be blank"),
                        new ErrorResponseData("email", "is invalid"),
                        new ErrorResponseData("body", "can't be blank")
                    ))
                    .build()
            },
            {
                CommentRequestData.builder()
                    .postId(null)
                    .name("")
                    .email(null)
                    .body("0".repeat(10000))
                    .build(),
                ResponseBody.<List<ErrorResponseData>>builder()
                    .meta(null)
                    .data(List.of(
                        new ErrorResponseData("post_id", "can't be blank"),
                        new ErrorResponseData("name", "can't be blank"),
                        new ErrorResponseData("email", "can't be blank"),
                        new ErrorResponseData("body", "is too long (maximum is 500 characters)")
                    ))
                    .build()
            },
            {
                CommentRequestData.builder()
                    .postId(postIds.get(1))
                    .name("    ")
                    .email("")
                    .body(null)
                    .build(),
                ResponseBody.<List<ErrorResponseData>>builder()
                    .meta(null)
                    .data(List.of(
                        new ErrorResponseData("name", "can't be blank"),
                        new ErrorResponseData("email", "can't be blank"),
                        new ErrorResponseData("body", "can't be blank")
                    ))
                    .build()
            },
            {
                CommentRequestData.builder()
                    .postId(null)
                    .name("0".repeat(10000))
                    .email("    ")
                    .body("")
                    .build(),
                ResponseBody.<List<ErrorResponseData>>builder()
                    .meta(null)
                    .data(List.of(
                        new ErrorResponseData("post_id", "can't be blank"),
                        new ErrorResponseData("name", "is too long (maximum is 200 characters)"),
                        new ErrorResponseData("email", "can't be blank"),
                        new ErrorResponseData("body", "can't be blank")
                    ))
                    .build()
            }
        };
    }

    @DataProvider
    private Object[][] getPostCommentObjectMissingFieldsData() {
        return new Object[][]{
            {
                CommentRequestData.builder()
                    .name(faker.name().fullName())
                    .email(faker.internet().emailAddress())
                    .body(faker.yoda().quote())
                    .build(),
                ResponseBody.<List<ErrorResponseData>>builder()
                    .meta(null)
                    .data(List.of(
                        new ErrorResponseData("post_id", "can't be blank")
                    ))
                    .build()
            },
            {
                CommentRequestData.builder()
                    .postId(postIds.get(1))
                    .email(faker.internet().emailAddress())
                    .body(faker.yoda().quote())
                    .build(),
                ResponseBody.<List<ErrorResponseData>>builder()
                    .meta(null)
                    .data(List.of(
                        new ErrorResponseData("name", "can't be blank")
                    ))
                    .build()
            },
            {
                CommentRequestData.builder()
                    .postId(postIds.get(0))
                    .name(faker.name().fullName())
                    .body(faker.yoda().quote())
                    .build(),
                ResponseBody.<List<ErrorResponseData>>builder()
                    .meta(null)
                    .data(List.of(
                        new ErrorResponseData("email", "can't be blank")
                    ))
                    .build()
            },
            {
                CommentRequestData.builder()
                    .postId(postIds.get(1))
                    .name(faker.name().fullName())
                    .email(faker.internet().emailAddress())
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
    private Object[][] getPutCommentObjectMissingFieldsData() {
        String name = faker.name().fullName();
        String body = faker.yoda().quote();
        Object[][] postData = new Object[][] {
            {postIds.get(0), faker.name().fullName(), faker.internet().emailAddress(), body},
            {postIds.get(1), faker.name().fullName(), faker.internet().emailAddress(), faker.yoda().quote()},
            {postIds.get(0), name, faker.internet().emailAddress(), faker.yoda().quote()},
            {postIds.get(1), faker.name().fullName(), faker.internet().emailAddress(), faker.yoda().quote()}
        };
        Object[][] putData = new Object[][] {
            {null, faker.name().fullName(), faker.internet().emailAddress(), body},
            {postIds.get(1), null, faker.internet().emailAddress(), faker.yoda().quote()},
            {postIds.get(1), name, null, faker.yoda().quote()},
            {postIds.get(1), faker.name().fullName(), faker.internet().emailAddress(), null}
        };
        return IntStream.range(0, Integer.min(postData.length, putData.length))
            .mapToObj(i -> new Object[]{
                CommentRequestData.builder()
                    .postId((Long) postData[i][0])
                    .name((String) postData[i][1])
                    .email((String) postData[i][2])
                    .body((String) postData[i][3])
                    .build(),
                CommentRequestData.builder()
                    .postId((Long) putData[i][0])
                    .name((String) putData[i][1])
                    .email((String) putData[i][2])
                    .body((String) putData[i][3])
                    .build(),
                ResponseBody.<CommentResponseData>builder()
                    .meta(null)
                    .data(
                        CommentResponseData.builder()
                            .postId((Long) (putData[i][0] == null ? postData[i][0] : putData[i][0]))
                            .name((String) (putData[i][1] == null ? postData[i][1] : putData[i][1]))
                            .email((String) (putData[i][2] == null ? postData[i][2] : putData[i][2]))
                            .body((String) (putData[i][3] == null ? postData[i][3] : putData[i][3]))
                            .build()
                    )
                    .build()
            })
            .toArray(Object[][]::new);
    }

    // ------------------------------
    // TESTS
    // ------------------------------

    @Test(dataProvider = "getPostCommentObjectData")
    public void testPostCommentObject(
        CommentRequestData requestBody, ResponseBody<CommentResponseData> expectedResponseBody
    ) {
        ResponseBody<CommentResponseData> actualResponseBody = actionStep.postObject(requestBody);
        allCreatedComments.add(actualResponseBody.getData().getId());
        assertionStep.assertObjectResponseBody(actualResponseBody, expectedResponseBody);
    }

    @Test(dataProvider = "getPostOrPutCommentObjectNegativeData")
    public void testPostCommentObjectNegative(
        CommentRequestData requestBody,
        ResponseBody<List<ErrorResponseData>> expectedResponseBody
    ) {
        ResponseBody<List<ErrorResponseData>> actualResponseBody = actionStep.postObjectWithErrors(requestBody);
        assertionStep.assertObjectResponseBodyWithErrors(actualResponseBody, expectedResponseBody);
    }

    @Test
    public void testPostCommentObjectDeletedPost() {
        ResponseBody<UserResponseData> userResponseBody = actionStep.postObject(
            UserRequestData.builder()
                .name(faker.name().fullName())
                .gender(Gender.FEMALE)
                .email(faker.internet().emailAddress())
                .status(Status.ACTIVE)
                .build()
        );
        Long userId = userResponseBody.getData().getId();
        ResponseBody<PostResponseData> postResponseBody = actionStep.postObject(
            PostRequestData.builder()
                .userId(userId)
                .title(faker.animal().name())
                .body(faker.yoda().quote())
                .build()
        );
        Long postId = postResponseBody.getData().getId();

        actionStep.deleteObject(postId, PostRequestData.class);
        actionStep.deleteObject(userId, UserRequestData.class);

        ResponseBody<List<ErrorResponseData>> actualResponseBody = actionStep.postObjectWithErrors(
            CommentRequestData.builder()
                .postId(postId)
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .body(faker.yoda().quote())
                .build()
        );
        ResponseBody<List<ErrorResponseData>> expectedResponseBody = ResponseBody.<List<ErrorResponseData>>builder()
            .meta(null)
            .data(List.of(
                new ErrorResponseData("post", "must exist")
            ))
            .build();
        assertionStep.assertObjectResponseBodyWithErrors(actualResponseBody, expectedResponseBody);
    }

    @Test(dataProvider = "getPostCommentObjectMissingFieldsData")
    public void testPostCommentObjectMissingFields(
        CommentRequestData requestBody,
        ResponseBody<List<ErrorResponseData>> expectedResponseBody
    ) {
        ResponseBody<List<ErrorResponseData>> actualResponseBody
            = actionStep.postObjectWithErrorsWithoutNullValues(requestBody);
        assertionStep.assertObjectResponseBodyWithErrors(actualResponseBody, expectedResponseBody);
    }

    @Test
    public void testPostCommentObjectInvalidRequestBody() {
        actionStep.postObjectInvalidRequestBody(CommentRequestData.class);
    }

    @Test(dataProvider = "getGetCommentObjectData")
    public void testGetCommentObject(CommentRequestData requestBody) {
        ResponseBody<CommentResponseData> responseBody = actionStep.postObject(requestBody);
        Long id = responseBody.getData().getId();
        allCreatedComments.add(id);
        ResponseBody<CommentResponseData> expectedResponseBody = ResponseBody.<CommentResponseData>builder()
            .meta(responseBody.getMeta())
            .data(responseBody.getData())
            .build();
        ResponseBody<CommentResponseData> actualResponseBody = actionStep.getObject(id, CommentRequestData.class);
        assertionStep.assertObjectResponseBody(actualResponseBody, expectedResponseBody);
    }

    @Test
    public void testGetDeletedCommentObject() {
        ResponseBody<CommentResponseData> responseBody = actionStep.postObject(
            CommentRequestData.builder()
                .postId(postIds.get(0))
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .body(faker.yoda().quote())
                .build()
        );
        Long id = responseBody.getData().getId();
        actionStep.deleteObject(id, CommentRequestData.class);

        ResponseBody<ErrorMessageResponseData> actualResponseBody
            = actionStep.getObjectWithError(id, CommentRequestData.class);
        assertionStep.assertResourceNotFoundError(actualResponseBody);
    }

    @Test
    public void testGetCommentObjectInvalidInput() {
        ResponseBody<ErrorMessageResponseData> actualResponseBody
            = actionStep.getObjectWithInvalidInput(CommentRequestData.class);
        assertionStep.assertResourceNotFoundError(actualResponseBody);
    }

    @Test
    public void testGetAllCommentObjectsFirstPage() {
        for (int i = 0; i < 20; i++) {
            ResponseBody<CommentResponseData> responseBody = actionStep.postObject(
                CommentRequestData.builder()
                    .postId(postIds.get(i % 2))
                    .name(faker.name().fullName())
                    .email(faker.internet().emailAddress())
                    .body(faker.yoda().quote())
                    .build()
            );
            allCreatedComments.add(responseBody.getData().getId());
        }

        ResponseBody<List<CommentResponseData>> actualResponseBody
            = actionStep.getObjectsFirstPage(CommentRequestData.class);
        assertionStep.assertObjectsFirstPageResponse(CommentRequestData.class, actualResponseBody);
    }

    @Test(dataProvider = "getPutCommentObjectData")
    public void testPutCommentObject(
        CommentRequestData requestBody, ResponseBody<CommentResponseData> expectedResponseBody
    ) {
        ResponseBody<CommentResponseData> postResponseBody = actionStep.postObject(
            CommentRequestData.builder()
                .postId(postIds.get(0))
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .body(faker.yoda().quote())
                .build()
        );
        Long id = postResponseBody.getData().getId();
        allCreatedComments.add(id);

        ResponseBody<CommentResponseData> actualResponseBody = actionStep.putObject(id, requestBody);
        assertionStep.assertObjectResponseBody(actualResponseBody, expectedResponseBody);
    }

    @Test(dataProvider = "getPostOrPutCommentObjectNegativeData")
    public void testPutCommentObjectNegative(
        CommentRequestData requestBody,
        ResponseBody<List<ErrorResponseData>> expectedResponseBody
    ) {
        ResponseBody<CommentResponseData> postResponseBody = actionStep.postObject(
            CommentRequestData.builder()
                .postId(postIds.get(0))
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .body(faker.yoda().quote())
                .build()
        );
        Long id = postResponseBody.getData().getId();
        allCreatedComments.add(id);

        ResponseBody<List<ErrorResponseData>> actualResponseBody = actionStep.putObjectWithErrors(id, requestBody);
        assertionStep.assertObjectResponseBodyWithErrors(actualResponseBody, expectedResponseBody);
    }

    @Test(dataProvider = "getPutCommentObjectMissingFieldsData")
    public void testPutCommentObjectMissingFields(
        CommentRequestData postRequestBody,
        CommentRequestData requestBody,
        ResponseBody<CommentResponseData> expectedResponseBody
    ) {
        ResponseBody<CommentResponseData> postResponseBody = actionStep.postObject(postRequestBody);
        Long id = postResponseBody.getData().getId();
        allCreatedComments.add(id);

        ResponseBody<CommentResponseData> actualResponseBody = actionStep.putObjectWithoutNullValues(id, requestBody);
        assertionStep.assertObjectResponseBody(actualResponseBody, expectedResponseBody);
    }

    @Test
    public void testPutDeletedCommentObject() {
        ResponseBody<CommentResponseData> postResponseBody = actionStep.postObject(
            CommentRequestData.builder()
                .postId(postIds.get(0))
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .body(faker.yoda().quote())
                .build()
        );
        Long id = postResponseBody.getData().getId();
        actionStep.deleteObject(id, CommentRequestData.class);

        ResponseBody<ErrorMessageResponseData> actualResponseBody = actionStep.putObjectWithError(
            id,
            CommentRequestData.builder()
                .postId(postIds.get(0))
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .body(faker.yoda().quote())
                .build()
        );
        assertionStep.assertResourceNotFoundError(actualResponseBody);
    }

    @Test
    public void testPutCommentObjectToDeletedPost() {
        ResponseBody<UserResponseData> userResponseBody = actionStep.postObject(
            UserRequestData.builder()
                .name(faker.name().fullName())
                .gender(Gender.FEMALE)
                .email(faker.internet().emailAddress())
                .status(Status.ACTIVE)
                .build()
        );
        Long userId = userResponseBody.getData().getId();
        ResponseBody<PostResponseData> postResponseBody = actionStep.postObject(
            PostRequestData.builder()
                .userId(userId)
                .title(faker.animal().name())
                .body(faker.yoda().quote())
                .build()
        );
        Long postId = postResponseBody.getData().getId();

        ResponseBody<CommentResponseData> responseBody = actionStep.postObject(
            CommentRequestData.builder()
                .postId(postIds.get(0))
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .body(faker.yoda().quote())
                .build()
        );
        Long id = responseBody.getData().getId();
        allCreatedComments.add(id);

        actionStep.deleteObject(postId, PostRequestData.class);
        actionStep.deleteObject(userId, UserRequestData.class);

        ResponseBody<List<ErrorResponseData>> actualResponseBody = actionStep.putObjectWithErrorsWithoutNullValues(
            id,
            CommentRequestData.builder()
                .postId(postId)
                .build()
        );
        ResponseBody<List<ErrorResponseData>> expectedResponseBody = ResponseBody.<List<ErrorResponseData>>builder()
            .meta(null)
            .data(List.of(
                new ErrorResponseData("post", "must exist")
            ))
            .build();
        assertionStep.assertObjectResponseBodyWithErrors(actualResponseBody, expectedResponseBody);
    }

    @Test
    public void testPutCommentObjectInvalidRequestBody() {
        ResponseBody<CommentResponseData> postResponseBody = actionStep.postObject(
            CommentRequestData.builder()
                .postId(postIds.get(0))
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .body(faker.yoda().quote())
                .build()
        );
        Long id = postResponseBody.getData().getId();
        allCreatedComments.add(id);

        actionStep.putObjectInvalidRequestBody(id, CommentRequestData.class);
    }

    @Test
    public void testPutCommentObjectInvalidInput() {
        ResponseBody<ErrorMessageResponseData> actualResponseBody
            = actionStep.putObjectWithInvalidInput(CommentRequestData.class);
        assertionStep.assertResourceNotFoundError(actualResponseBody);
    }

    @Test
    public void testDeleteCommentObject() {
        ResponseBody<CommentResponseData> responseBody = actionStep.postObject(
            CommentRequestData.builder()
                .postId(postIds.get(0))
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .body(faker.yoda().quote())
                .build()
        );
        Long id = responseBody.getData().getId();
        actionStep.deleteObject(id, CommentRequestData.class);

        ResponseBody<ErrorMessageResponseData> actualGetResponseBody
            = actionStep.getObjectWithError(id, CommentRequestData.class);
        assertionStep.assertResourceNotFoundError(actualGetResponseBody);
    }

    @Test
    public void testDeleteDeletedCommentObject() {
        ResponseBody<CommentResponseData> responseBody = actionStep.postObject(
            CommentRequestData.builder()
                .postId(postIds.get(0))
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .body(faker.yoda().quote())
                .build()
        );
        Long id = responseBody.getData().getId();
        actionStep.deleteObject(id, CommentRequestData.class);

        ResponseBody<ErrorMessageResponseData> actualDeleteResponseBody
            = actionStep.deleteObjectWithError(id, CommentRequestData.class);
        assertionStep.assertResourceNotFoundError(actualDeleteResponseBody);
    }

    @Test
    public void testDeleteCommentObjectInvalidInput() {
        ResponseBody<ErrorMessageResponseData> actualResponseBody
            = actionStep.deleteObjectWithInvalidInput(CommentRequestData.class);
        assertionStep.assertResourceNotFoundError(actualResponseBody);
    }

}
