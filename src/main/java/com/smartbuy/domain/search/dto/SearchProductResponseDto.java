package com.smartbuy.domain.search.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchProductResponseDto {
    private String title;
    private String link;
    private String image;
    private int lprice;
    private String mallName;
}
