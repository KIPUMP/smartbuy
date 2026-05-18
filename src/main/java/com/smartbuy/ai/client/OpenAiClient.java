package com.smartbuy.ai.client;

import com.smartbuy.ai.dto.openai.OpenAiChatRequestDto;
import com.smartbuy.ai.dto.openai.OpenAiChatResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class OpenAiClient {
    private final RestClient.Builder restClientBuilder;

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.base-url}")
    private String baseUrl;

    public String generateRecommendation(String prompt) {
        RestClient restClient = restClientBuilder.build();

        OpenAiChatRequestDto request = new OpenAiChatRequestDto(
                model,
                java.util.List.of(
                        new OpenAiChatRequestDto.Message(
                                "system",
                                "당신은 쇼핑 상품 비교 전문가입니다. 사용자가 쉽게 선택할 수 있도록 간결하고 실용적인 추천 문장을 작성하세요."
                        ),
                        new OpenAiChatRequestDto.Message(
                                "user",
                                prompt
                        )
                ),
                0.3
        );

        OpenAiChatResponseDto response = restClient.post()
                .uri(baseUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(request)
                .retrieve()
                .body(OpenAiChatResponseDto.class);

        if (response == null || response.choices() == null || response.choices().isEmpty()) {
            return "추천 문장을 생성하지 못했습니다.";
        }

        System.out.println(response);

        return response.choices().get(0).message().content();
    }

}
