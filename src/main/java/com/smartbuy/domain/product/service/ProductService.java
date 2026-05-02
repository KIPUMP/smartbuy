package com.smartbuy.domain.product.service;

import com.smartbuy.domain.product.dto.ProductResponseDto;
import com.smartbuy.domain.product.entity.Product;
import com.smartbuy.domain.product.repository.ProductRepository;
import com.smartbuy.global.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponseDto getProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        return ProductResponseDto.from(product);
    }
}
