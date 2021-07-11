package ru.levelp.at.hw9.steps;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.api.SoftAssertions;
import ru.levelp.at.hw9.RequestUriUtil;
import ru.levelp.at.hw9.templates.response.Links;
import ru.levelp.at.hw9.templates.response.Metadata;
import ru.levelp.at.hw9.templates.response.Pagination;
import ru.levelp.at.hw9.templates.response.ResponseBody;
import ru.levelp.at.hw9.templates.response.data.ErrorMessageResponseData;
import ru.levelp.at.hw9.templates.response.data.ErrorResponseData;
import ru.levelp.at.hw9.templates.response.data.ObjectResponseData;

public class AssertionStep {

    public <R extends ObjectResponseData> void assertObjectResponseBody(
        ResponseBody<R> actual,
        ResponseBody<R> expected
    ) {
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actual)
                  .usingRecursiveComparison()
                  .ignoringExpectedNullFields()
                  .isEqualTo(expected);
            softly.assertThat(actual)
                  .extracting(ResponseBody::getMeta)
                  .isNull();
            softly.assertThat(actual)
                  .extracting(r -> r.getData().getId())
                  .isNotNull();
        });
    }

    public void assertObjectResponseBodyWithErrors(
        ResponseBody<List<ErrorResponseData>> actual,
        ResponseBody<List<ErrorResponseData>> expected
    ) {
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actual)
                  .usingRecursiveComparison()
                  .ignoringExpectedNullFields()
                  .ignoringCollectionOrder()
                  .isEqualTo(expected);
            softly.assertThat(actual)
                  .extracting(ResponseBody::getMeta)
                  .isNull();
        });
    }

    public void assertResourceNotFoundError(ResponseBody<ErrorMessageResponseData> actual) {
        assertThat(actual).isEqualTo(
            ResponseBody.<ErrorMessageResponseData>builder()
                .meta(null)
                .data(new ErrorMessageResponseData("Resource not found"))
                .build()
        );
    }

    @SuppressWarnings("unchecked")
    public <T, R extends ObjectResponseData> void assertObjectsFirstPageResponse(
        Class<T> requestDataClass, ResponseBody<List<R>> actual
    ) {
        if (actual.getData() == null || actual.getData().isEmpty()) {
            ResponseBody<List<R>> expected = ResponseBody.<List<R>>builder()
                .meta(new Metadata(
                    Pagination
                        .builder()
                        .page(1).limit(20)
                        .links(Links
                            .builder()
                            .current(RequestUriUtil.getPageUri(1, requestDataClass))
                            .build()
                        )
                        .build()
                ))
                .data(Collections.emptyList())
                .build();
            assertThat(actual).isEqualTo(expected);
        } else {
            Class<R> responseBodyClass = (Class<R>) actual.getData().get(0).getClass();
            List<Long> userIds = actual.getData().stream()
                .map(R::getId)
                .collect(Collectors.toList());

            ResponseBody<List<R>> expected = ResponseBody.<List<R>>builder()
                 .meta(new Metadata(
                     Pagination
                         .builder()
                         .page(1).limit(20)
                         .links(Links
                             .builder()
                             .current(RequestUriUtil.getPageUri(1, requestDataClass))
                             .build()
                         )
                         .build()
                 ))
                 .data(userIds.stream()
                      .map(id -> {
                          String responseBody = given()
                              .pathParam("id", id)
                              .when()
                              .get(RequestUriUtil.OBJECT_BY_ID_API_URL.get(requestDataClass))
                              .then()
                              .statusCode(200)
                              .extract()
                              .jsonPath()
                              .prettify();
                          return ResponseBody.createFromString(responseBody, responseBodyClass).getData();
                      })
                      .collect(Collectors.toList())
                 )
                 .build();

            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(actual)
                      .usingRecursiveComparison()
                      .ignoringExpectedNullFields()
                      .ignoringCollectionOrder()
                      .isEqualTo(expected);
                softly.assertThat(actual)
                      .extracting(r -> r.getMeta().getPagination().getTotal())
                      .isNotNull();
                softly.assertThat(actual)
                      .extracting(r -> r.getMeta().getPagination().getPages())
                      .isNotNull();
                softly.assertThat(actual)
                      .extracting(r -> {
                          Pagination pagination = r.getMeta().getPagination();
                          return pagination.getTotal() <= pagination.getPages() * pagination.getLimit()
                              && pagination.getTotal() > (pagination.getPages() - 1) * pagination.getLimit();
                      })
                      .isEqualTo(true);
                softly.assertThat(actual)
                      .extracting(r -> r.getMeta().getPagination().getLinks().getPrevious())
                      .isNull();
                if (actual.getMeta().getPagination().getPages() == 1) {
                    softly.assertThat(actual)
                          .extracting(r -> r.getMeta().getPagination().getLinks().getNext())
                          .isNull();
                } else {
                    softly.assertThat(actual)
                          .extracting(r -> r.getMeta().getPagination().getLinks().getNext())
                          .isEqualTo(RequestUriUtil.getPageUri(2, requestDataClass));
                }
            });
        }
    }

}
