package com.smartbuy.global.exception;
/*
*  역할 : 상품을 찾을 수 없을 떄 사용하는 예외
*  기능 : 상품 조회 실패 상황을 명확하게 표현
* */
public class ProductNotFoundException extends BaseException{
    public ProductNotFoundException() {
        super("PRODUCT_NOT_FOUND", "상품을 찾을 수 없습니다");
    }
}
