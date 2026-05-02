package com.smartbuy.intergration.shopping.naver.client;

import com.smartbuy.intergration.shopping.naver.dto.NaverShoppingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class NaverShoppingClient {
    private final RestClient restClient;

    @Value("${naver.shopping.base-url}")
    private String baseUrl;

    @Value("${naver.shopping.client-id}")
    private String clientId;

    @Value("${naver.shopping.client-secret}")
    private String clientSecret;

    public NaverShoppingResponseDto search(String query) {
        return restClient.get()
                .uri(baseUrl + "?query={query}&display=10&sort=sim", query)
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .retrieve()
                .body(NaverShoppingResponseDto.class);
    }

}
