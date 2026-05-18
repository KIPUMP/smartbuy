package com.smartbuy.domain.search.service;

import com.smartbuy.ai.dto.AiSearchQueryDto;
import com.smartbuy.ai.service.AiSearchService;
import com.smartbuy.domain.history.service.SearchHistoryService;
import com.smartbuy.domain.search.dto.SearchProductResponseDto;
import com.smartbuy.domain.search.dto.SearchResponseDto;
import com.smartbuy.intergration.shopping.naver.client.NaverShoppingClient;
import com.smartbuy.intergration.shopping.naver.dto.NaverShoppingItemDto;
import com.smartbuy.intergration.shopping.naver.dto.NaverShoppingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final NaverShoppingClient naverShoppingClient;
    private final AiSearchService aiSearchService;
    private final SearchHistoryService searchHistoryService;
    private final RedisTemplate<String, SearchResponseDto> redisTemplate;

    public SearchResponseDto search(
            String keyword,
            Integer minPrice,
            Integer maxPrice,
            String sort,
            int page,
            int size
    ) {
        String cacheKey = createCacheKey(keyword, minPrice, maxPrice, sort, page, size);

        SearchResponseDto cachedResponse = redisTemplate.opsForValue().get(cacheKey);
        if (cachedResponse != null) {
            return cachedResponse;
        }

        AiSearchQueryDto aiSearchQuery = aiSearchService.refineSearchKeyword(keyword);
        String refinedKeyword = aiSearchQuery.getKeyword();

        NaverShoppingResponseDto response =
                naverShoppingClient.search(refinedKeyword);

        List<SearchProductResponseDto> products = response.getItems().stream()
                .filter(item -> parsePrice(item.getLprice()) > 0)
                .map(this::toProductResponse)
                .collect(Collectors.toMap(
                        product -> normalizeTitle(product.getTitle()),
                        product -> product,
                        (existing, replacement) -> existing
                ))
                .values()
                .stream()
                .toList();

        List<SearchProductResponseDto> filteredProducts =
                filterByPrice(products, minPrice, maxPrice);

        List<SearchProductResponseDto> sortedProducts =
                sortProducts(filteredProducts, sort);

        List<SearchProductResponseDto> pagedProducts =
                paginate(sortedProducts, page, size);

        SearchProductResponseDto lowestProduct = filteredProducts.stream()
                .min(Comparator.comparingInt(SearchProductResponseDto::getPrice))
                .orElse(null);

        if (lowestProduct != null) {
            searchHistoryService.saveHistory(
                    keyword,
                    refinedKeyword,
                    lowestProduct.getTitle(),
                    lowestProduct.getPrice()
            );
        }

       String recommendation = aiSearchService.generateShoppingRecommendation(keyword, pagedProducts);

        SearchResponseDto responseDto = SearchResponseDto.builder()
                .keyword(keyword)
                .refinedKeyword(refinedKeyword)
                .recommendation(recommendation)
                .page(page)
                .size(size)
                .totalCount(sortedProducts.size())
                .products(pagedProducts)
                .build();

        redisTemplate.opsForValue().set(
                cacheKey,
                responseDto,
                Duration.ofMinutes(3)
        );

        return responseDto;
    }

    private SearchProductResponseDto toProductResponse(NaverShoppingItemDto item) {
        return SearchProductResponseDto.builder()
                .title(cleanTitle(item.getTitle()))
                .link(item.getLink())
                .image(item.getImage())
                .price(parsePrice(item.getLprice()))
                .mallName(item.getMallName())
                .build();
    }

    private List<SearchProductResponseDto> filterByPrice(
            List<SearchProductResponseDto> products,
            Integer minPrice,
            Integer maxPrice
    ) {
        return products.stream()
                .filter(product -> minPrice == null || product.getPrice() >= minPrice)
                .filter(product -> maxPrice == null || product.getPrice() <= maxPrice)
                .toList();
    }

    private List<SearchProductResponseDto> sortProducts(
            List<SearchProductResponseDto> products,
            String sort
    ) {
        return switch (sort) {
            case "priceDesc" -> products.stream()
                    .sorted(Comparator.comparingInt(SearchProductResponseDto::getPrice).reversed())
                    .toList();

            case "mall" -> products.stream()
                    .sorted(Comparator.comparing(SearchProductResponseDto::getMallName))
                    .toList();

            case "priceAsc" -> products.stream()
                    .sorted(Comparator.comparingInt(SearchProductResponseDto::getPrice))
                    .toList();

            default -> products.stream()
                    .sorted(Comparator.comparingInt(SearchProductResponseDto::getPrice))
                    .toList();
        };
    }

    private List<SearchProductResponseDto> paginate(
            List<SearchProductResponseDto> products,
            int page,
            int size
    ) {
        int start = page * size;
        int end = Math.min(start + size, products.size());

        if (start >= products.size()) {
            return List.of();
        }

        return products.subList(start, end);
    }

    private String cleanTitle(String title) {
        if (title == null) {
            return "";
        }

        return title
                .replaceAll("<[^>]*>", "")
                .replace("&quot;", "\"")
                .replace("&amp;", "&");
    }

    private String normalizeTitle(String title) {
        if (title == null) {
            return "";
        }

        return title
                .replaceAll("<[^>]*>", "")
                .replaceAll("[^가-힣a-zA-Z0-9]", "")
                .toLowerCase();
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

    private String createCacheKey(
            String keyword,
            Integer minPrice,
            Integer maxPrice,
            String sort,
            int page,
            int size
    ) {
        return "search:" +
                keyword + ":" +
                minPrice + ":" +
                maxPrice + ":" +
                sort + ":" +
                page + ":" +
                size;
    }
}