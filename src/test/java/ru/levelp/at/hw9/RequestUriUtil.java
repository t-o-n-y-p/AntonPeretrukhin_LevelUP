package ru.levelp.at.hw9;

import java.util.Map;
import ru.levelp.at.hw9.templates.request.data.CommentRequestData;
import ru.levelp.at.hw9.templates.request.data.PostRequestData;
import ru.levelp.at.hw9.templates.request.data.UserRequestData;

public final class RequestUriUtil {

    private RequestUriUtil() {
    }

    public static final String BASE_URL = "https://gorest.co.in";

    public static final Map<Class<?>, String> OBJECTS_API_URL = Map.of(
        UserRequestData.class, "/public/v1/users",
        PostRequestData.class, "/public/v1/posts",
        CommentRequestData.class, "/public/v1/comments"
    );
    public static final Map<Class<?>, String> OBJECTS_PAGE_API_URL = Map.of(
        UserRequestData.class, "/public/v1/users?page=%d",
        PostRequestData.class, "/public/v1/posts?page=%d",
        CommentRequestData.class, "/public/v1/comments?page=%d"
    );
    public static final Map<Class<?>, String> OBJECT_BY_ID_API_URL = Map.of(
        UserRequestData.class, "/public/v1/users/{id}",
        PostRequestData.class, "/public/v1/posts/{id}",
        CommentRequestData.class, "/public/v1/comments/{id}"
    );
    public static final Map<Class<?>, String> OBJECT_BY_ID_INVALID_INPUT_API_URL = Map.of(
        UserRequestData.class, "/public/v1/users/qwerty",
        PostRequestData.class, "/public/v1/posts/qwerty",
        CommentRequestData.class, "/public/v1/comments/qwerty"
    );

    public static <T> String getPageUri(int page, Class<T> requestDataClass) {
        return String.format(BASE_URL + OBJECTS_PAGE_API_URL.get(requestDataClass), page);
    }

}
