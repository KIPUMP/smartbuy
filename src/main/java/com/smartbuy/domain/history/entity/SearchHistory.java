package com.smartbuy.domain.history.entity;

import com.smartbuy.domain.user.entity.User;
import com.smartbuy.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "search_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String originalQuery;

    @Column(nullable = false, length = 500)
    private String refinedQuery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public SearchHistory(String originalQuery, String refinedQuery, User user) {
        this.originalQuery = originalQuery;
        this.refinedQuery = refinedQuery;
        this.user = user;
    }

    @Builder
    public SearchHistory(String originalQuery, String refinedQuery) {
        this.originalQuery = originalQuery;
        this.refinedQuery = refinedQuery;
    }

}
