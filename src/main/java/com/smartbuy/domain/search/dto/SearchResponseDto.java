package com.smartbuy.domain.search.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SearchResponseDto {
    private String keyword;

    private String refinedKeyword;

    private int page;

    private int size;

    private int totalCount;

    private List<SearchProductResponseDto> products;
}
