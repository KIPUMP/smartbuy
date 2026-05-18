package com.smartbuy.ai.dto.openai;

import java.util.List;

public record OpenAiChatResponseDto(List<Choice> choices) {
    public record Choice(Message message){}
    public record Message(String role, String content){}
}
