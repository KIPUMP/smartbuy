package com.smartbuy.domain.history.repository;

import com.smartbuy.domain.history.dto.SearchRankingResponseDto;
import com.smartbuy.domain.history.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long>{
    List<SearchHistory> findTop10ByOrderByCreatedAtDesc();

    @Query("""
            SELECT new com.smartbuy.domain.history.dto.SearchRankingResponseDto(
                h.originalQuery,
                COUNT(h)
            )
            FROM SearchHistory h
            GROUP BY h.originalQuery
            ORDER BY COUNT(h) DESC
            """)
    List<SearchRankingResponseDto> findSearchRanking();
}
