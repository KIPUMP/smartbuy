package com.smartbuy.domain.search.service;

import com.smartbuy.ai.dto.AiSearchQueryDto;
import com.smartbuy.ai.service.AiSearchService;
import com.smartbuy.domain.history.service.SearchHistoryService;
import com.smartbuy.domain.search.dto.SearchProductResponseDto;
import com.smartbuy.domain.search.dto.SearchResponseDto;
import com.smartbuy.domain.search.dto.SearchResultDto;
import com.smartbuy.intergration.shopping.naver.client.NaverShoppingClient;
import com.smartbuy.intergration.shopping.naver.dto.NaverShoppingItemDto;
import com.smartbuy.intergration.shopping.naver.dto.NaverShoppingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final NaverShoppingClient naverShoppingClient;
    private final AiSearchService aiSearchService;
    private final SearchHistoryService searchHistoryService;

    public List<SearchProductResponseDto> searchLowestPriceProducts(String keyword) {
        NaverShoppingResponseDto response = naverShoppingClient.search(keyword);

        List<NaverShoppingItemDto> items = response.getItems();

        return items.stream()
                .filter(item -> parsePrice(item.getLprice()) > 0)
                .map(item -> SearchProductResponseDto.builder()
                        .title(removeHtmlTags(item.getTitle()))
                        .link(item.getLink())
                        .image(item.getImage())
                        .lprice(parsePrice(item.getLprice()))
                        .mallName(item.getMallName())
                        .build())
                .collect(Collectors.toMap(
                        SearchProductResponseDto::getTitle,
                        product -> product,
                        (existing, replacement) -> existing
                ))
                .values()
                .stream()
                .sorted(Comparator.comparingInt(SearchProductResponseDto::getLprice))
                .limit(5)
                .toList();
    }

    public SearchResponseDto search(String query) {
        NaverShoppingResponseDto response = naverShoppingClient.search(query);
        AiSearchQueryDto aiResult = aiSearchService.refinedQuery(query);

        List<SearchResultDto> products = response.getItems().stream()
                .map(this::toSearchResultDto)
                .toList();

        SearchResultDto lowestProduct = products.stream()
                .min(Comparator.comparingInt(SearchResultDto::getPrice))
                .orElseThrow(() -> new IllegalArgumentException("검색 결과가 없습니다."));

        searchHistoryService.saveHistory(
                aiResult.getOriginalQuery(),
                aiResult.getRefinedQuery(),
                lowestProduct.getTitle(),
                lowestProduct.getPrice()
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
                .mailName(item.getMallName())
                .imageUrl(item.getImage())
                .productUrl(item.getLink())
                .build();
    }

    private String removeHtmlTags(String title){
        if(title == null){
            return "";
        }
        return title.replaceAll("<[^>]*>", "");
    }

    private int parsePrice(String price) {
        if (price == null || price.isBlank()) {
            return 0;
        }

        try {
            return Integer.parseInt(price);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
