package ru.levelp.at.hw9.steps;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.assertj.core.api.SoftAssertions;
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

    public <R extends ObjectResponseData> void assertObjectsFirstPageResponse(
        ResponseBody<List<R>> actual, ResponseBody<List<R>> expected, String expectedSecondPageUrl
    ) {
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
                      .isEqualTo(expectedSecondPageUrl);
            }
        });
    }

}
