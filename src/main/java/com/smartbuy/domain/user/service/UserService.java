package com.smartbuy.domain.user.service;

import com.smartbuy.domain.user.dto.LoginRequestDto;
import com.smartbuy.domain.user.dto.LoginResponseDto;
import com.smartbuy.domain.user.dto.SignUpRequestDto;
import com.smartbuy.domain.user.dto.UserResponseDto;
import com.smartbuy.domain.user.entity.User;
import com.smartbuy.domain.user.repository.UserRepository;
import com.smartbuy.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserResponseDto signUp(SignUpRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다");
        }
        User user = User.create(
                requestDto.getEmail(),
                passwordEncoder.encode(requestDto.getPassword()),
                requestDto.getNickname()
        );

        return UserResponseDto.from(userRepository.save(user));
    }

    public LoginResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다"));

        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        String accessToken = jwtTokenProvider.createToken(user.getId(), user.getEmail());

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }

}
