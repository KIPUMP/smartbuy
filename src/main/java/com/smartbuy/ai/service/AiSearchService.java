package com.smartbuy.ai.service;

import com.smartbuy.ai.client.OpenAiClient;
import com.smartbuy.ai.dto.AiSearchQueryDto;
import com.smartbuy.domain.search.dto.SearchProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiSearchService {

    private final OpenAiClient openAiClient;

    public AiSearchQueryDto refineSearchKeyword(String userQuery) {
        return AiSearchQueryDto.builder()
                .originalQuery(userQuery)
                .keyword(userQuery)
                .build();
    }

    public String generateShoppingRecommendation(String keyword, List<SearchProductResponseDto> products) {
        if (products == null || products.isEmpty()) {
            return "검색 조건에 맞는 상품이 없어 추천 문장을 제공하기 어렵습니다.";
        }

        String productSummary = products.stream()
                .limit(5)
                .map(product -> String.format(
                        "- 상품명: %s, 가격: %d원, 쇼핑몰: %s",
                        product.getTitle(),
                        product.getPrice(),
                        product.getMallName()
                ))
                .reduce("", (a, b) -> a + "\n" + b);

        String prompt = """
                사용자가 '%s' 상품을 검색했습니다.

                아래 상품 목록을 비교해서 사용자가 선택하기 쉬운 추천 문장을 작성해주세요.

                조건:
                - 한국어로 작성
                - 2문장 이내
                - 가장 저렴한 상품만 무조건 추천하지 말고, 가격과 쇼핑몰 정보를 함께 고려
                - 과장 표현 금지
                - 상품명이 너무 길면 핵심 이름만 사용

                상품 목록:
                %s
                """.formatted(keyword, productSummary);

        try {
            return openAiClient.generateRecommendation(prompt);
        } catch (Exception e) {

            SearchProductResponseDto lowestProduct = products.stream()
                    .min(Comparator.comparingInt(SearchProductResponseDto::getPrice))
                    .orElse(null);

            if (lowestProduct == null) {
                return "추천 상품을 찾을 수 없습니다.";
            }

            return String.format(
                    "현재 검색 결과 기준으로 '%s' 상품이 %d원으로 가장 저렴합니다.",
                    lowestProduct.getTitle(),
                    lowestProduct.getPrice()
            );
        }
    }
}
