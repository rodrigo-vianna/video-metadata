package com.goldmediatech.videometadata.web.controller;

import com.goldmediatech.videometadata.domain.dto.VideoDto;
import com.goldmediatech.videometadata.service.VideoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class VideoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VideoService videoService;

    private VideoDto getSampleVideoDto() {
        return VideoDto.builder()
                .id(1L)
                .title("Sample Title")
                .description("A sample video")
                .durationSeconds(360)
                .viewCount(100L)
                .likeCount(10L)
                .source("youtube")
                .externalId("yt123")
                .thumbnailUrl("thumb.png")
                .videoUrl("video.mp4")
                .channelName("channel")
                .tags("test,unit")
                .uploadDate(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("GET /videos/{id} should return the video")
    void testGetVideoById() throws Exception {
        VideoDto video = getSampleVideoDto();
        Mockito.when(videoService.getVideoById(1L)).thenReturn(video);

        mockMvc.perform(get("/videos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(video.getTitle()));
    }
}
