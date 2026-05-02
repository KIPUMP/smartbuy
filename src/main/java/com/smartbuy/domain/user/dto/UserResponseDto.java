package com.smartbuy.domain.user.dto;

import com.smartbuy.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private Long id;
    private String email;
    private String name;

    public static UserResponseDto from(User user) {
        return UserResponseDto.builder().id(user.getId())
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
