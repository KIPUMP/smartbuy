package com.smartbuy.web.controller;

import com.smartbuy.domain.history.dto.SearchHistoryResponseDto;
import com.smartbuy.domain.history.service.SearchHistoryService;
import com.smartbuy.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search-histories")
@RequiredArgsConstructor
public class SearchHistoryController {
    private final SearchHistoryService searchHistoryService;

    @GetMapping("/recent")
    public ApiResponse<List<SearchHistoryResponseDto>> getRecentHistories() {
        return ApiResponse.ok(searchHistoryService.getRecentHistories());
    }
}
