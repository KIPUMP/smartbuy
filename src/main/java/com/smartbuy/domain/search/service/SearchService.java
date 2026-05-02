package com.smartbuy.domain.search.service;

import com.smartbuy.ai.dto.AiSearchQueryDto;
import com.smartbuy.ai.service.AiSearchService;
import com.smartbuy.domain.history.service.SearchHistoryService;
import com.smartbuy.domain.search.dto.SearchResponseDto;
import com.smartbuy.domain.search.dto.SearchResultDto;
import com.smartbuy.intergration.shopping.naver.client.NaverShoppingClient;
import com.smartbuy.intergration.shopping.naver.dto.NaverShoppingItemDto;
import com.smartbuy.intergration.shopping.naver.dto.NaverShoppingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final NaverShoppingClient naverShoppingClient;
    private final AiSearchService aiSearchService;
    private final SearchHistoryService searchHistoryService;

    public SearchResponseDto search(String query) {
        NaverShoppingResponseDto response = naverShoppingClient.search(query);
        AiSearchQueryDto aiResult = aiSearchService.refinedQuery(query);

        List<SearchResultDto> products = response.getItems().stream()
                .map(this::toSearchResultDto)
                .toList();

        searchHistoryService.saveHistory(
                aiResult.getOriginalQuery(),
                aiResult.getRefinedQuery()
        );

        return SearchResponseDto.builder()
                .originalQuery(aiResult.getOriginalQuery())
                .refinedQuery(aiResult.getRefinedQuery())
                .products(products)
                .build();
    }

    private SearchResultDto toSearchResultDto(NaverShoppingItemDto item) {
        return SearchResultDto.builder()
                .title(item.getTitle())
                .price(Integer.parseInt(item.getLprice()))
                .mailName(item.getMailName())
                .imageUrl(item.getImage())
                .productUrl(item.getLink())
                .build();
    }
}
