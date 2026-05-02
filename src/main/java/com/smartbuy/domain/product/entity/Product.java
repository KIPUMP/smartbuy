package com.smartbuy.domain.product.entity;

import com.smartbuy.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String title;

    @Column(nullable = false)
    private Integer price;

    @Column(length = 1000)
    private String imageUrl;

    @Column(length = 1000)
    private String productUrl;

    @Column(length = 100)
    private String mallName;

    @Builder
    public Product(String title, Integer price, String imageUrl, String productUrl, String mallName) {
        this.title = title;
        this.price = price;
        this.imageUrl = imageUrl;
        this.productUrl = productUrl;
        this.mallName = mallName;
    }
}
