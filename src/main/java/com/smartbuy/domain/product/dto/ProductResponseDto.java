package com.smartbuy.domain.product.dto;

import com.smartbuy.domain.product.entity.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductResponseDto {
    private Long id;
    private String title;
    private Integer price;
    private String imageUrl;
    private String productUrl;
    private String emailName;

    public static ProductResponseDto from(Product product){
        return ProductResponseDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .productUrl(product.getProductUrl())
                .emailName(product.getMallName())
                .build();
    }
}
