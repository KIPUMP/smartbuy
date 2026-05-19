package com.smartbuy.integration.shopping.naver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class NaverShoppingResponseDto {
    private List<NaverShoppingItemDto> items;
}
