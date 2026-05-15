package com.smartbuy.web.controller;

import com.smartbuy.domain.search.dto.SearchProductResponseDto;
import com.smartbuy.domain.search.dto.SearchRequestDto;
import com.smartbuy.domain.search.dto.SearchResponseDto;
import com.smartbuy.domain.search.service.SearchService;
import com.smartbuy.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public ApiResponse<SearchResponseDto> search(
            @RequestParam String keyword,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(defaultValue = "priceAsc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.ok(
                searchService.search(keyword, minPrice, maxPrice, sort, page, size)
        );
    }
}
