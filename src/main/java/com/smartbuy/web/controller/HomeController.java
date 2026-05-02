package com.smartbuy.web.controller;

import com.smartbuy.global.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public ApiResponse<String> home() {
        return ApiResponse.ok("SmartBuy 서버가 정상 실행 중입니다.");
    }
}
