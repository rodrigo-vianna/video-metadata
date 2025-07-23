package com.goldmediatech.videometadata.web.controller;

import com.goldmediatech.videometadata.domain.dto.AuthRequestDto;
import com.goldmediatech.videometadata.domain.dto.AuthResponseDto;
import com.goldmediatech.videometadata.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication operations for obtaining JWT tokens")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(
        summary = "User authentication",
        description = "Authenticates user credentials and returns a JWT token for accessing protected endpoints"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Authentication successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Invalid credentials",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request format",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto authRequest) {
        logger.info("Authentication attempt for user: {}", authRequest.getUsername());
        
        try {
            AuthResponseDto response = authService.authenticate(authRequest);
            logger.info("User authenticated successfully: {}", authRequest.getUsername());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.warn("Authentication failed for user: {} - {}", authRequest.getUsername(), e.getMessage());
            throw e;
        }
    }

    @GetMapping("/health")
    @Operation(
        summary = "Authentication service health check",
        description = "Returns the status of the authentication service"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Service is healthy",
        content = @Content(mediaType = "application/json")
    )
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("{\"status\": \"Authentication service is running\"}");
    }
}