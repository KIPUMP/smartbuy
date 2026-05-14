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

    @PostMapping
    public ApiResponse<SearchResponseDto> search(@RequestBody SearchRequestDto request) {
        return ApiResponse.ok(searchService.search(request.getQuery()));
    }

    @GetMapping
    public ApiResponse<List<SearchProductResponseDto>> search(
            @RequestParam String keyword
    ) {
        return ApiResponse.ok(searchService.searchLowestPriceProducts(keyword));
    }
}
