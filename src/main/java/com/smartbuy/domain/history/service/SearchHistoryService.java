package com.smartbuy.domain.history.service;

import com.smartbuy.domain.history.dto.SearchHistoryResponseDto;
import com.smartbuy.domain.history.dto.SearchRankingResponseDto;
import com.smartbuy.domain.history.entity.SearchHistory;
import com.smartbuy.domain.history.repository.SearchHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchHistoryService {
    private final SearchHistoryRepository searchHistoryRepository;

    @Transactional
    public void saveHistory(String originalQuery,
                            String refinedQuery,
                            String lowestProductName,
                            Integer lowestPrice) {
        searchHistoryRepository.save(
                SearchHistory.builder()
                        .originalQuery(originalQuery)
                        .refinedQuery(refinedQuery)
                        .lowestProductName(lowestProductName)
                        .lowestPrice(lowestPrice)
                        .build()
        );
    }

    public List<SearchHistoryResponseDto> getRecentHistories() {
        return searchHistoryRepository.findTop10ByOrderByCreatedAtDesc()
                .stream()
                .map(SearchHistoryResponseDto::from)
                .toList();
    }

    public List<SearchRankingResponseDto> getSearchRanking() {
        return searchHistoryRepository.findSearchRanking()
                .stream()
                .limit(10)
                .toList();
    }


}
