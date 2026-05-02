package com.smartbuy.global.handler;
/*
*  역할 : 프로젝트 전체 예외를 한 곳에서 처리
*  기능 : Controller나 Service에서 예외가 발생하면 이 클래스가 잡아서 응답으로 바꿔줌
* */

import com.smartbuy.global.exception.BaseException;
import com.smartbuy.global.response.ApiResponse;
import com.smartbuy.global.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handlerBaseException(BaseException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(new ErrorResponse(e.getCode(), e.getMessage())));
    }

    public ResponseEntity<ApiResponse<Void>> handleException(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(new ErrorResponse("INTERNAL_SERVER_ERROR","서버 내부 오류가 발생했습니다")));
    }
}
