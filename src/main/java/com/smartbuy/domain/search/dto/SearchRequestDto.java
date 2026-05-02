package com.smartbuy.domain.search.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchRequestDto {
    @NotBlank(message = "검색어는 필수입니다.")
    private String query;
}
