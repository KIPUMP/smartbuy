package com.smartbuy.domain.search.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SearchResponseDto {
    private String originalQuery;
    private String refinedQuery;
    private List<SearchResultDto> products;
}
