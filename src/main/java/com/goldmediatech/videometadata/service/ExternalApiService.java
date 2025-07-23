package com.goldmediatech.videometadata.service;

import com.goldmediatech.videometadata.domain.dto.VideoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class ExternalApiService {

    private final Random random = new Random();

    /**
     * Simulates fetching videos from external APIs.
     *
     * @param source The video platform source
     * @return List of VideoDto objects from the external source
     */
    public List<VideoDto> fetchVideos(String source) {
        log.info("Fetching videos from external API: {}", source);

        // Simulate API delay
        try {
            Thread.sleep(1000 + random.nextInt(2000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String sourceLower = source.toLowerCase();
        if ("youtube".equals(sourceLower)) {
            return generateYouTubeVideos();
        }
        if ("vimeo".equals(sourceLower)) {
            return generateVimeoVideos();
        }
        if ("internal".equals(sourceLower)) {
            return generateInternalVideos();
        }
        throw new IllegalArgumentException("Unsupported source: " + source);

    }

    private List<VideoDto> generateYouTubeVideos() {
        return List.of(
                VideoDto.builder()
                        .title("Advanced Java Programming Tutorial")
                        .description("Learn advanced Java concepts and best practices")
                        .durationSeconds(3600)
                        .viewCount(125000L)
                        .likeCount(8500L)
                        .source("youtube")
                        .externalId("yt_" + System.currentTimeMillis())
                        .thumbnailUrl("https://img.youtube.com/vi/example1/maxresdefault.jpg")
                        .videoUrl("https://www.youtube.com/watch?v=example1")
                        .channelName("Tech Education Channel")
                        .tags("java,programming,tutorial,advanced")
                        .build(),

                VideoDto.builder()
                        .title("Spring Boot Microservices Architecture")
                        .description("Complete guide to building microservices with Spring Boot")
                        .durationSeconds(4200)
                        .viewCount(89000L)
                        .likeCount(6200L)
                        .source("youtube")
                        .externalId("yt_" + (System.currentTimeMillis() + 1))
                        .thumbnailUrl("https://img.youtube.com/vi/example2/maxresdefault.jpg")
                        .videoUrl("https://www.youtube.com/watch?v=example2")
                        .channelName("Spring Framework Official")
                        .tags("spring,microservices,architecture,java")
                        .build());
    }

    private List<VideoDto> generateVimeoVideos() {
        return List.of(
                VideoDto.builder()
                        .title("Creative Coding with Processing")
                        .description("Artistic programming using Processing framework")
                        .durationSeconds(2700)
                        .viewCount(45000L)
                        .likeCount(3200L)
                        .source("vimeo")
                        .externalId("vimeo_" + System.currentTimeMillis())
                        .thumbnailUrl("https://vimeo.com/example1/thumbnail")
                        .videoUrl("https://vimeo.com/example1")
                        .channelName("Creative Coders")
                        .tags("processing,creative,coding,art")
                        .build());
    }

    private List<VideoDto> generateInternalVideos() {
        return List.of(
                VideoDto.builder()
                        .title("Company Training: API Security Best Practices")
                        .description("Internal training on secure API development")
                        .durationSeconds(1800)
                        .viewCount(150L)
                        .likeCount(25L)
                        .source("internal")
                        .externalId("internal_" + System.currentTimeMillis())
                        .thumbnailUrl("/internal/thumbnails/security-training.jpg")
                        .videoUrl("/internal/videos/security-training.mp4")
                        .channelName("Company Training Department")
                        .tags("security,api,training,internal")
                        .build());
    }
}
