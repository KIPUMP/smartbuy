package com.smartbuy.domain.user.service;

import com.smartbuy.domain.user.dto.UserResponseDto;
import com.smartbuy.domain.user.entity.User;
import com.smartbuy.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserResponseDto createUser(String email, String name) {
        User user = User.builder()
                .email(email)
                .name(name)
                .build();

        return UserResponseDto.from(userRepository.save(user));
    }

}
