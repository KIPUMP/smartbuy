package com.smartbuy.web.controller;


import com.smartbuy.domain.search.dto.SearchResponseDto;
import com.smartbuy.domain.search.service.SearchService;
import com.smartbuy.domain.user.entity.User;
import com.smartbuy.domain.user.repository.UserRepository;
import com.smartbuy.global.response.ApiResponse;
import com.smartbuy.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @GetMapping
    public ApiResponse<SearchResponseDto> search(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam String keyword,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(defaultValue = "priceAsc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        User user = null;
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            String token = jwtTokenProvider.resolveToken(authorizationHeader);

            if (token != null && !token.isBlank()) {
                if (jwtTokenProvider.validateToken(token)) {
                    Long userId = jwtTokenProvider.getUserId(token);
                    user = userRepository.findById(userId)
                            .orElse(null);
                }
            }
        }

        SearchResponseDto response = searchService.search(keyword, minPrice, maxPrice, sort, page, size, user);
        return ApiResponse.ok(response);
    }
}
