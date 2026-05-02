package com.smartbuy.ai.service;

import com.smartbuy.ai.dto.AiSearchQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiSearchService {

    public AiSearchQueryDto refinedQuery(String userQuery) {
        return AiSearchQueryDto.builder()
                .originalQuery(userQuery)
                .refinedQuery(userQuery)
                .build();
    }
}
