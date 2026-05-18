package com.smartbuy.ai.dto.openai;

import java.util.List;

public record OpenAiChatRequestDto(String model, List<Message> messages, double temperature) {
    public record Message(String role, String content){

    }
}
