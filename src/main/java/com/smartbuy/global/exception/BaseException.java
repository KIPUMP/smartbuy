package com.smartbuy.global.exception;
/*
*  역할 : 프로젝트에서 사용하는 커스텀 예외 부모 클래스
*  기능 : 모든 비즈니스 예외가 공통적으로 code, message를 가지게 함
* */
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final String code;
    private final String message;

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
