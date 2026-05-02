package com.smartbuy.domain.history.dto;

import com.smartbuy.domain.history.entity.SearchHistory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SearchHistoryResponseDto {
    private Long id;
    private String originalQuery;
    private String refinedQuery;
    private LocalDateTime createdAt;

    public static SearchHistoryResponseDto from(SearchHistory history){
        return SearchHistoryResponseDto.builder()
                .id(history.getId())
                .originalQuery(history.getOriginalQuery())
                .refinedQuery(history.getRefinedQuery())
                .createdAt(history.getCreateAt())
                .build();
    }
}
