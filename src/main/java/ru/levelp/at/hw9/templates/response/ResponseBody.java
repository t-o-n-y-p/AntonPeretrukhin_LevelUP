package ru.levelp.at.hw9.templates.response;

import ru.levelp.at.hw9.templates.response.data.RestData;

public class ResponseBody<T extends RestData> {

    private Integer code;
    private Metadata meta;
    private T data;

}
