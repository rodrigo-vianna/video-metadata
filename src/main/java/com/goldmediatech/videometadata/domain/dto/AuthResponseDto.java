package com.goldmediatech.videometadata.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {

    private String token;
    private String type = "Bearer";
    private String username;
    private String role;
    private Long expiresIn; // seconds

    public static AuthResponseDtoBuilder builder() {
        return new AuthResponseDtoBuilder();
    }


    public static class AuthResponseDtoBuilder {
        private AuthResponseDto authResponseDto;

        public AuthResponseDtoBuilder() {
            this.authResponseDto = new AuthResponseDto();
        }

        public AuthResponseDtoBuilder token(String token) {
            authResponseDto.setToken(token);
            return this;
        }

        public AuthResponseDtoBuilder type(String type) {
            authResponseDto.setType(type);
            return this;
        }

        public AuthResponseDtoBuilder username(String username) {
            authResponseDto.setUsername(username);
            return this;
        }

        public AuthResponseDtoBuilder role(String role) {
            authResponseDto.setRole(role);
            return this;
        }

        public AuthResponseDtoBuilder expiresIn(Long expiresIn) {
            authResponseDto.setExpiresIn(expiresIn);
            return this;
        }

        public AuthResponseDto build() {
            return authResponseDto;
        }
    }   
}
