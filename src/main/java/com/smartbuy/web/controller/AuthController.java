package com.smartbuy.web.controller;

import com.smartbuy.domain.user.dto.LoginRequestDto;
import com.smartbuy.domain.user.dto.LoginResponseDto;
import com.smartbuy.domain.user.dto.SignUpRequestDto;
import com.smartbuy.domain.user.dto.UserResponseDto;
import com.smartbuy.domain.user.service.UserService;
import com.smartbuy.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/signup")
    public ApiResponse<UserResponseDto> signUp(@RequestBody SignUpRequestDto requestDto){
        return ApiResponse.ok(userService.signUp(requestDto));
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        return ApiResponse.ok(userService.login(requestDto));
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout() {
        return ApiResponse.ok("로그아웃이 완료되었습니다");
    }

}
