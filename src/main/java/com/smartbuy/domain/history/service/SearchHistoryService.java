package com.smartbuy.domain.history.service;

import com.smartbuy.domain.history.dto.SearchHistoryResponseDto;
import com.smartbuy.domain.history.dto.SearchRankingResponseDto;
import com.smartbuy.domain.history.entity.SearchHistory;
import com.smartbuy.domain.history.repository.SearchHistoryRepository;
import com.smartbuy.domain.user.entity.User;
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
                            Integer lowestPrice, User user) {

        searchHistoryRepository.save(
                SearchHistory.builder()
                        .originalQuery(originalQuery)
                        .refinedQuery(refinedQuery)
                        .lowestProductName(lowestProductName)
                        .lowestPrice(lowestPrice)
                        .user(user)
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

    public void saveSearchHistory(String query, User user) {
        SearchHistory history = SearchHistory.builder()
                .originalQuery(query)
                .user(user)
                .build();

        searchHistoryRepository.save(history);
    }
}
