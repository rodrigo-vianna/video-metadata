package com.goldmediatech.videometadata.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Video Metadata Service API",
        version = "1.0.0",
        description = """
            A secure backend service for managing and analyzing video metadata from multiple platforms.
            
            ## Features
            - JWT-based authentication and authorization
            - Video metadata CRUD operations
            - External API integration for video data import
            - Analytics and statistics generation
            - Role-based access control (ADMIN, USER)
            
            ## Authentication
            This API uses JWT (JSON Web Tokens) for authentication. To access protected endpoints:
            1. Obtain a token by calling POST /auth/login with valid credentials
            2. Include the token in the Authorization header as: `Bearer <your-token>`
            
            ## Default Test Users
            - **Admin**: username=`admin`, password=`admin123`
            - **User**: username=`user`, password=`user123`
            """,
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(
            url = "http://localhost:8080/api",
            description = "Development Server"
        ),
    }
)
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    description = "JWT token for API authentication. Obtain token from /auth/login endpoint."
)
public class OpenApiConfig {
    
    // Additional OpenAPI customization can be added here if needed
}