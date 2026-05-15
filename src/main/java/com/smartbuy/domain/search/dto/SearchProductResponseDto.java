package com.smartbuy.domain.search.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode(of = {"title", "price", "mallName"})
public class SearchProductResponseDto {
    private String title;
    private String link;
    private String image;
    private int price;
    private String mallName;
}
