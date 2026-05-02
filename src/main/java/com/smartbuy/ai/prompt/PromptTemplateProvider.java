package com.smartbuy.ai.prompt;

import org.springframework.stereotype.Component;

@Component
public class PromptTemplateProvider {
    public String buildSearchPrompt(String userQuery) {
        return """
                사용자의 쇼핑 검색 요청을 분석해서
                실제 쇼핑몰 검색에 적합한 짧고 명확한 검색어 1개만 만들어주세요.

                사용자 입력:
                %s

                응답 형식:
                검색어만 출력
                """.formatted(userQuery);
    }
}
