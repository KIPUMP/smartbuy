package com.smartbuy.global.handler;

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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(new ErrorResponse(e.getCode(), e.getMessage())));
    }
}
