package com.smartbuy.global.response;
/*
*  역할 : 에러 응답 정보를 담음
*  기능 : 에러 코드와 메세지를 관리
* */
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;
}
