package com.smartbuy.ai.service;

import com.smartbuy.ai.dto.AiSearchQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiSearchService {

    public AiSearchQueryDto refineSearchKeyword(String userQuery) {
        return AiSearchQueryDto.builder()
                .originalQuery(userQuery)
                .keyword(userQuery)
                .build();
    }
}
