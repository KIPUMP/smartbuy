package com.smartbuy.domain.history.dto;

import com.smartbuy.domain.history.entity.SearchHistory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
public record SearchHistoryResponseDto (Long id,
                                        String originalQuery,
                                        String refinedQuery,
                                        String lowestProductName,
                                        Integer lowestPrice,
                                        LocalDateTime createdAt) {

    public static SearchHistoryResponseDto from(SearchHistory history){
        return SearchHistoryResponseDto.builder()
                .id(history.getId())
                .originalQuery(history.getOriginalQuery())
                .refinedQuery(history.getRefinedQuery())
                .createdAt(history.getCreatedAt())
                .build();
    }
}
