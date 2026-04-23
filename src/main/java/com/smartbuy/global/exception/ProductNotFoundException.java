package com.smartbuy.global.exception;

public class ProductNotFoundException extends BaseException{
    public ProductNotFoundException() {
        super("PRODUCT_NOT_FOUND", "상품을 찾을 수 없습니다");
    }
}
