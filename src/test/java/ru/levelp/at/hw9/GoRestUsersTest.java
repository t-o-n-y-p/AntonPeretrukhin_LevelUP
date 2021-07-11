package ru.levelp.at.hw9;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.levelp.at.hw9.templates.Gender;
import ru.levelp.at.hw9.templates.Status;
import ru.levelp.at.hw9.templates.request.data.UserRequestData;
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
        ResponseBody<UserResponseData> actualResponseBody = actionStep.postObject(requestBody);
        allCreatedUsers.add(actualResponseBody.getData().getId());
        assertionStep.assertObjectResponseBody(actualResponseBody, expectedResponseBody);
    }

    @Test(dataProvider = "getPostOrPutUserObjectNegativeData")
    public void testPostUserObjectNegative(
        UserRequestData requestBody,
        ResponseBody<List<ErrorResponseData>> expectedResponseBody
    ) {
        ResponseBody<List<ErrorResponseData>> actualResponseBody = actionStep.postObjectWithErrors(requestBody);
        assertionStep.assertObjectResponseBodyWithErrors(actualResponseBody, expectedResponseBody);
    }

    @Test
    public void testPostUserObjectEmailAlreadyExists() {
        String email = faker.internet().emailAddress();
        ResponseBody<UserResponseData> responseBody = actionStep.postObject(
            UserRequestData.builder()
                .name(faker.name().fullName())
                .gender(Gender.FEMALE)
                .email(email)
                .status(Status.ACTIVE)
                .build()
        );
        allCreatedUsers.add(responseBody.getData().getId());

        ResponseBody<List<ErrorResponseData>> actualResponseBody = actionStep.postObjectWithErrors(
            UserRequestData.builder()
                .name(faker.name().fullName())
                .gender(Gender.MALE)
                .email(email)
                .status(Status.INACTIVE)
                .build()
        );
        ResponseBody<List<ErrorResponseData>> expectedResponseBody = ResponseBody.<List<ErrorResponseData>>builder()
            .meta(null)
            .data(List.of(
                new ErrorResponseData("email", "has already been taken")
            ))
            .build();
        assertionStep.assertObjectResponseBodyWithErrors(actualResponseBody, expectedResponseBody);
    }

    @Test(dataProvider = "getPostUserObjectMissingFieldsData")
    public void testPostUserObjectMissingFields(
        UserRequestData requestBody,
        ResponseBody<List<ErrorResponseData>> expectedResponseBody
    ) {
        ResponseBody<List<ErrorResponseData>> actualResponseBody
            = actionStep.postObjectWithErrorsWithoutNullValues(requestBody);
        assertionStep.assertObjectResponseBodyWithErrors(actualResponseBody, expectedResponseBody);
    }

    @Test
    public void testPostUserObjectInvalidRequestBody() {
        actionStep.postObjectInvalidRequestBody(UserRequestData.class);
    }

    @Test(dataProvider = "getGetUserObjectData")
    public void testGetUserObject(UserRequestData requestBody) {
        ResponseBody<UserResponseData> responseBody = actionStep.postObject(requestBody);
        Long id = responseBody.getData().getId();
        allCreatedUsers.add(id);
        ResponseBody<UserResponseData> expectedResponseBody = ResponseBody.<UserResponseData>builder()
            .meta(responseBody.getMeta())
            .data(responseBody.getData())
            .build();
        ResponseBody<UserResponseData> actualResponseBody = actionStep.getObject(id, UserRequestData.class);
        assertionStep.assertObjectResponseBody(actualResponseBody, expectedResponseBody);
    }

    @Test
    public void testGetDeletedUserObject() {
        ResponseBody<UserResponseData> responseBody = actionStep.postObject(
            UserRequestData.builder()
                .name(faker.name().fullName())
                .gender(Gender.FEMALE)
                .email(faker.internet().emailAddress())
                .status(Status.ACTIVE)
                .build()
        );
        Long id = responseBody.getData().getId();
        actionStep.deleteObject(id, UserRequestData.class);
        ResponseBody<ErrorMessageResponseData> actualResponseBody
            = actionStep.getObjectWithError(id, UserRequestData.class);
        assertionStep.assertResourceNotFoundError(actualResponseBody);
    }

    @Test
    public void testGetUserObjectInvalidInput() {
        ResponseBody<ErrorMessageResponseData> actualResponseBody
            = actionStep.getObjectWithInvalidInput(UserRequestData.class);
        assertionStep.assertResourceNotFoundError(actualResponseBody);
    }

    @Test
    public void testGetAllUserObjectsFirstPage() {
        for (int i = 0; i < 20; i++) {
            ResponseBody<UserResponseData> responseBody = actionStep.postObject(
                UserRequestData.builder()
                    .name(faker.name().fullName())
                    .gender(Gender.FEMALE)
                    .email(faker.internet().emailAddress())
                    .status(Status.ACTIVE)
                    .build()
            );
            Long id = responseBody.getData().getId();
            allCreatedUsers.add(id);
        }

        ResponseBody<List<UserResponseData>> actualResponseBody = actionStep.getObjectsFirstPage(UserRequestData.class);
        assertionStep.assertObjectsFirstPageResponse(UserRequestData.class, actualResponseBody);
    }

    @Test(dataProvider = "getPutUserObjectData")
    public void testPutUserObject(UserRequestData requestBody, ResponseBody<UserResponseData> expectedResponseBody) {
        ResponseBody<UserResponseData> responseBody = actionStep.postObject(
            UserRequestData.builder()
                .name(faker.name().fullName())
                .gender(Gender.MALE)
                .email(faker.internet().emailAddress())
                .status(Status.INACTIVE)
                .build()
        );
        Long id = responseBody.getData().getId();
        allCreatedUsers.add(id);

        ResponseBody<UserResponseData> actualResponseBody = actionStep.putObject(id, requestBody);
        assertionStep.assertObjectResponseBody(actualResponseBody, expectedResponseBody);
    }

    @Test(dataProvider = "getPostOrPutUserObjectNegativeData")
    public void testPutUserObjectNegative(
        UserRequestData requestBody,
        ResponseBody<List<ErrorResponseData>> expectedResponseBody
    ) {
        ResponseBody<UserResponseData> responseBody = actionStep.postObject(
            UserRequestData.builder()
                .name(faker.name().fullName())
                .gender(Gender.MALE)
                .email(faker.internet().emailAddress())
                .status(Status.INACTIVE)
                .build()
        );
        Long id = responseBody.getData().getId();
        allCreatedUsers.add(id);
        ResponseBody<List<ErrorResponseData>> actualResponseBody = actionStep.putObjectWithErrors(id, requestBody);
        assertionStep.assertObjectResponseBodyWithErrors(actualResponseBody, expectedResponseBody);
    }

    @Test(dataProvider = "getPutUserObjectMissingFieldsData")
    public void testPutUserObjectMissingFields(
        UserRequestData postRequestBody,
        UserRequestData requestBody,
        ResponseBody<UserResponseData> expectedResponseBody
    ) {
        ResponseBody<UserResponseData> responseBody = actionStep.postObject(postRequestBody);
        Long id = responseBody.getData().getId();
        allCreatedUsers.add(id);

        ResponseBody<UserResponseData> actualResponseBody = actionStep.putObjectWithoutNullValues(id, requestBody);
        assertionStep.assertObjectResponseBody(actualResponseBody, expectedResponseBody);
    }

    @Test
    public void testPutDeletedUserObject() {
        ResponseBody<UserResponseData> responseBody = actionStep.postObject(
            UserRequestData.builder()
                .name(faker.name().fullName())
                .gender(Gender.FEMALE)
                .email(faker.internet().emailAddress())
                .status(Status.ACTIVE)
                .build()
        );
        Long id = responseBody.getData().getId();
        actionStep.deleteObject(id, UserRequestData.class);

        ResponseBody<ErrorMessageResponseData> actualResponseBody = actionStep.putObjectWithError(
            id,
            UserRequestData.builder()
                .name(faker.name().fullName())
                .gender(Gender.FEMALE)
                .email(faker.internet().emailAddress())
                .status(Status.ACTIVE)
                .build()
        );
        assertionStep.assertResourceNotFoundError(actualResponseBody);
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
                return responseBody.getData();
            })
            .collect(Collectors.toList());

        ResponseBody<List<ErrorResponseData>> actualResponseBody = actionStep.putObjectWithErrorsWithoutNullValues(
            responseBodies.get(1).getId(),
            UserRequestData.builder()
                .email(responseBodies.get(0).getEmail())
                .build()
        );
        ResponseBody<List<ErrorResponseData>> expectedResponseBody = ResponseBody.<List<ErrorResponseData>>builder()
            .meta(null)
            .data(List.of(
                new ErrorResponseData("email", "has already been taken")
            ))
            .build();
        assertionStep.assertObjectResponseBodyWithErrors(actualResponseBody, expectedResponseBody);
    }

    @Test
    public void testPutUserObjectInvalidRequestBody() {
        ResponseBody<UserResponseData> responseBody = actionStep.postObject(
            UserRequestData.builder()
                .name(faker.name().fullName())
                .gender(Gender.FEMALE)
                .email(faker.internet().emailAddress())
                .status(Status.ACTIVE)
                .build()
        );
        Long id = responseBody.getData().getId();
        allCreatedUsers.add(id);
        actionStep.putObjectInvalidRequestBody(id, UserRequestData.class);
    }

    @Test
    public void testPutUserObjectInvalidInput() {
        ResponseBody<ErrorMessageResponseData> actualResponseBody
            = actionStep.putObjectWithInvalidInput(UserRequestData.class);
        assertionStep.assertResourceNotFoundError(actualResponseBody);
    }

    @Test
    public void testDeleteUserObject() {
        ResponseBody<UserResponseData> responseBody = actionStep.postObject(
            UserRequestData.builder()
                .name(faker.name().fullName())
                .gender(Gender.FEMALE)
                .email(faker.internet().emailAddress())
                .status(Status.ACTIVE)
                .build()
        );
        Long id = responseBody.getData().getId();
        actionStep.deleteObject(id, UserRequestData.class);

        ResponseBody<ErrorMessageResponseData> actualGetResponseBody
            = actionStep.getObjectWithError(id, UserRequestData.class);
        assertionStep.assertResourceNotFoundError(actualGetResponseBody);
    }

    @Test
    public void testDeleteDeletedUserObject() {
        ResponseBody<UserResponseData> responseBody = actionStep.postObject(
            UserRequestData.builder()
                .name(faker.name().fullName())
                .gender(Gender.FEMALE)
                .email(faker.internet().emailAddress())
                .status(Status.ACTIVE)
                .build()
        );
        Long id = responseBody.getData().getId();
        actionStep.deleteObject(id, UserRequestData.class);

        ResponseBody<ErrorMessageResponseData> actualDeleteResponseBody
            = actionStep.deleteObjectWithError(id, UserRequestData.class);
        assertionStep.assertResourceNotFoundError(actualDeleteResponseBody);
    }

    @Test
    public void testDeleteUserObjectInvalidInput() {
        ResponseBody<ErrorMessageResponseData> actualResponseBody
            = actionStep.deleteObjectWithInvalidInput(UserRequestData.class);
        assertionStep.assertResourceNotFoundError(actualResponseBody);
    }

}
