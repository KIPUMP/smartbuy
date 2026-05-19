package com.smartbuy.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    private String accessToken;
    private String tokenType;
    private Long userId;
    private String email;
    private String nickname;
}
