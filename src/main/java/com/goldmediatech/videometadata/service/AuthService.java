package com.goldmediatech.videometadata.service;

import com.goldmediatech.videometadata.domain.dto.AuthRequestDto;
import com.goldmediatech.videometadata.domain.dto.AuthResponseDto;
import com.goldmediatech.videometadata.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDto authenticate(AuthRequestDto authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()));

            User user = (User) authentication.getPrincipal();

            String jwtToken = jwtService.generateToken(user);

            return AuthResponseDto.builder()
                    .username(user.getUsername())
                    .role(user.getRole().name())
                    .token(jwtToken)
                    .expiresIn(jwtService.extractExpiration(jwtToken).getTime())
                    .build();
        } catch (AuthenticationException ex) {
            log.warn("Authentication failed for user: {}", authRequest.getUsername());
            throw new RuntimeException("Invalid username or password");
        }
    }

}
