package ru.levelp.at.hw9.templates.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Builder
@ToString
public class Pagination {

    private Integer total;
    private Integer pages;
    private Integer page;
    private Integer limit;
    private Links links;

}
