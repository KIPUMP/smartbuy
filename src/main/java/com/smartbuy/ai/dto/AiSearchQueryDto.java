package com.smartbuy.ai.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiSearchQueryDto {
    private String originalQuery;
    private String refinedQuery;
}
