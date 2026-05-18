package com.smartbuy.domain.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponseDto implements Serializable {
    private String keyword;

    private String refinedKeyword;

    private String recommendation;

    private int page;

    private int size;

    private int totalCount;

    private List<SearchProductResponseDto> products;
}
