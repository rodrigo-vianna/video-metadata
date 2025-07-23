-- Insert default users for testing
-- The password hash corresponds to 'admin123' and 'user123' respectively
INSERT INTO users (username, password, email, role, created_at, updated_at, account_non_expired, account_non_locked, credentials_non_expired, enabled) 
VALUES 
('admin', '$2a$12$jM8NKmlgfRd/tRFixjYI4Oou6Fhl2/0/onvgIOLoeBloeENI23LGi', 'admin@goldmediatech.com', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, true, true, true),
('user', '$2a$12$26r5aiN2XHXWgmAB5for/eeJHY8QqLcP6Gpf60lpPTEjjGZAGUEwW', 'user@goldmediatech.com', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, true, true, true);

INSERT INTO videos (title, description, external_id, source, thumbnail_url, video_url, duration_seconds, upload_date, view_count, like_count, channel_name, channel_id, tags, created_at, updated_at)
VALUES 
('Spring Boot Tutorial - Getting Started', 'Complete tutorial on Spring Boot fundamentals and setup', 'YT001', 'youtube', 
 'https://img.youtube.com/vi/YT001/maxresdefault.jpg', 'https://youtube.com/watch?v=YT001', 1200, 
 '2024-01-15 10:30:00', 15000, 450, 'Tech Education Channel', 'UCTechEd123', 'spring,boot,java,tutorial,programming', 
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('JWT Authentication in Spring Security', 'Learn how to implement JWT authentication with Spring Security 6', 'YT002', 'youtube',
 'https://img.youtube.com/vi/YT002/maxresdefault.jpg', 'https://youtube.com/watch?v=YT002', 2100,
 '2024-01-20 14:45:00', 8500, 320, 'Security Experts', 'UCSecExp456', 'jwt,spring,security,authentication',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Microservices Architecture Patterns', 'Deep dive into microservices design patterns and best practices', 'YT003', 'youtube',
 'https://img.youtube.com/vi/YT003/maxresdefault.jpg', 'https://youtube.com/watch?v=YT003', 3600,
 '2024-02-01 09:15:00', 22000, 680, 'Architecture Insights', 'UCArchIn789', 'microservices,architecture,patterns,design',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Advanced Java Concurrency', 'Master concurrent programming in Java with practical examples', 'VM001', 'vimeo',
 'https://i.vimeocdn.com/video/VM001_640.jpg', 'https://vimeo.com/VM001', 2800,
 '2024-01-25 16:20:00', 3200, 85, 'Java Masters', 'VMJavaMst', 'java,concurrency,threading,programming',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Database Design Best Practices', 'Learn professional database design principles and normalization', 'VM002', 'vimeo',
 'https://i.vimeocdn.com/video/VM002_640.jpg', 'https://vimeo.com/VM002', 2400,
 '2024-02-05 11:30:00', 1800, 62, 'Database Pros', 'VMDbPros', 'database,design,sql,normalization',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Company Onboarding - Development Setup', 'Step-by-step guide for setting up development environment', 'INT001', 'internal',
 'https://internal.goldmediatech.com/thumb/INT001.jpg', 'https://internal.goldmediatech.com/video/INT001', 900,
 '2024-01-10 08:00:00', 150, 12, 'GoldMediaTech Training', 'GMTTraining', 'onboarding,setup,development,internal',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Code Review Guidelines', 'Best practices for conducting effective code reviews', 'INT002', 'internal',
 'https://internal.goldmediatech.com/thumb/INT002.jpg', 'https://internal.goldmediatech.com/video/INT002', 1800,
 '2024-01-18 13:45:00', 95, 8, 'GoldMediaTech Training', 'GMTTraining', 'code-review,guidelines,best-practices',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('API Documentation with OpenAPI', 'How to create comprehensive API documentation using OpenAPI 3.0', 'INT003', 'internal',
 'https://internal.goldmediatech.com/thumb/INT003.jpg', 'https://internal.goldmediatech.com/video/INT003', 1500,
 '2024-02-08 10:15:00', 72, 6, 'GoldMediaTech Training', 'GMTTraining', 'api,documentation,openapi,swagger',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Docker Containerization Guide', 'Complete guide to containerizing applications with Docker', 'YT004', 'youtube',
 'https://img.youtube.com/vi/YT004/maxresdefault.jpg', 'https://youtube.com/watch?v=YT004', 2700,
 '2024-02-12 15:30:00', 11000, 380, 'DevOps Academy', 'UCDevOpsAc', 'docker,containers,devops,deployment',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('RESTful API Design Principles', 'Learn how to design clean and maintainable REST APIs', 'YT005', 'youtube',
 'https://img.youtube.com/vi/YT005/maxresdefault.jpg', 'https://youtube.com/watch?v=YT005', 1950,
 '2024-02-18 12:00:00', 6700, 245, 'API Design Hub', 'UCApiDes', 'rest,api,design,principles,http',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
