package com.smartbuy.domain.search.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchResultDto {
    private String title;
    private Integer price;
    private String mailName;
    private String imageUrl;
    private String productUrl;
}
