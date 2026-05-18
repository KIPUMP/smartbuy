package com.smartbuy.domain.search.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"title", "price", "mallName"})
public class SearchProductResponseDto implements Serializable {
    private String title;
    private String link;
    private String image;
    private int price;
    private String mallName;
}
