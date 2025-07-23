package com.goldmediatech.videometadata.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.goldmediatech.videometadata.domain.dto.VideoDto;
import com.goldmediatech.videometadata.domain.entity.Video;
import com.goldmediatech.videometadata.persistence.repository.VideoRepository;
import com.goldmediatech.videometadata.util.VideoMapper;
import com.goldmediatech.videometadata.web.exception.ResourceNotFoundException;
import com.goldmediatech.videometadata.web.exception.DuplicateResourceException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private VideoMapper videoMapper;

    @Mock
    private ExternalApiService externalApiService;

    @InjectMocks
    private VideoService videoService;

    private Video video;
    private VideoDto videoDto;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
        video = new Video();
        video.setId(1L);
        video.setTitle("Test Video");
        video.setExternalId("ext1");
        video.setSource("youtube");

        videoDto = VideoDto.builder()
                .id(1L)
                .title("Test Video")
                .externalId("ext1")
                .source("youtube")
                .build();
    }

    @Test
    @DisplayName("Test getAllVideos without filters")
    void testGetAllVideosWithoutFilters() {
        Page<Video> page = new PageImpl<>(List.of(video));
        when(videoRepository.findAll(pageable)).thenReturn(page);
        when(videoMapper.toDto(video)).thenReturn(videoDto);

        Page<VideoDto> result = videoService.getAllVideos(null, null, null, null, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(videoDto.getTitle(), result.getContent().get(0).getTitle());
        verify(videoRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Test getAllVideos with filters")
    void testGetAllVideosWithFilters() {
        Page<Video> page = new PageImpl<>(List.of(video));
        when(videoRepository.findByFilters("youtube", null, null, null, pageable)).thenReturn(page);
        when(videoMapper.toDto(video)).thenReturn(videoDto);

        Page<VideoDto> result = videoService.getAllVideos("youtube", null, null, null, pageable);

        assertEquals(1, result.getTotalElements());
        verify(videoRepository, times(1)).findByFilters("youtube", null, null, null, pageable);
    }

    @Test
    @DisplayName("Test getVideoById found")
    void testGetVideoByIdFound() {
        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));
        when(videoMapper.toDto(video)).thenReturn(videoDto);

        VideoDto result = videoService.getVideoById(1L);

        assertNotNull(result);
        assertEquals(videoDto.getTitle(), result.getTitle());
    }

    @Test
    @DisplayName("Test getVideoById not found")
    void testGetVideoByIdNotFound() {
        when(videoRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> videoService.getVideoById(2L));
    }

    @Test
    @DisplayName("Test createVideo success")
    void testCreateVideoSuccess() {
        when(videoRepository.findByExternalIdAndSource("ext1", "youtube")).thenReturn(Optional.empty());
        when(videoMapper.toEntity(videoDto)).thenReturn(video);
        when(videoRepository.save(any())).thenReturn(video);
        when(videoMapper.toDto(video)).thenReturn(videoDto);

        VideoDto created = videoService.createVideo(videoDto);

        assertNotNull(created);
        assertEquals(videoDto.getExternalId(), created.getExternalId());
        verify(videoRepository).save(any(Video.class));
    }

    @Test
    @DisplayName("Test createVideo duplicate")
    void testCreateVideoDuplicate() {
        when(videoRepository.findByExternalIdAndSource("ext1", "youtube")).thenReturn(Optional.of(video));

        assertThrows(DuplicateResourceException.class, () -> videoService.createVideo(videoDto));
        verify(videoRepository, never()).save(any(Video.class));
    }

    @Test
    @DisplayName("Test deleteVideo success")
    void testDeleteVideoSuccess() {
        when(videoRepository.existsById(1L)).thenReturn(true);

        videoService.deleteVideo(1L);

        verify(videoRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Test deleteVideo not found")
    void testDeleteVideoNotFound() {
        when(videoRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> videoService.deleteVideo(1L));
    }

    @Test
    @DisplayName("Test importVideosFromExternalApi valid")
    void testImportVideosFromExternalApiValid() {
        VideoDto externalVideo = VideoDto.builder()
                .externalId("ext2")
                .source("youtube")
                .title("External Video")
                .build();

        when(externalApiService.fetchVideos("youtube")).thenReturn(List.of(externalVideo));
        when(videoRepository.findByExternalIdAndSource("ext2", "youtube")).thenReturn(Optional.empty());
        when(videoMapper.toEntity(externalVideo)).thenReturn(new Video());
        when(videoRepository.save(any(Video.class))).thenAnswer(invocation -> {
            Video v = invocation.getArgument(0);
            v.setId(2L);
            return v;
        });
        when(videoMapper.toDto(any(Video.class))).thenReturn(externalVideo);

        List<VideoDto> imported = videoService.importVideosFromExternalApi("youtube");

        assertEquals(1, imported.size());
        verify(externalApiService).fetchVideos("youtube");
    }

    @Test
    @DisplayName("Test importVideosFromExternalApi invalid source")
    void testImportVideosFromExternalApiInvalidSource() {
        assertThrows(IllegalArgumentException.class, () -> videoService.importVideosFromExternalApi("invalid"));
    }

    @Test
    @DisplayName("Test getVideoStatistics")
    void testGetVideoStatistics() {
        var statsRaw = new Object[] { "youtube", 5L, 300.0 };
        var statsRaw2 = new Object[] { "vimeo", 10L, 200.0 };
        when(videoRepository.getStatisticsBySource()).thenReturn(Arrays.asList(statsRaw, statsRaw2));

        var result = videoService.getVideoStatistics();

        assertEquals(2, result.size());
        assertEquals("youtube", result.get(0).getSource());
        assertEquals(5L, result.get(0).getTotalVideos());
    }

    @Test
    @DisplayName("Test searchVideosByTitle with term")
    void testSearchVideosByTitleWithTerm() {
        Page<Video> page = new PageImpl<>(List.of(video));
        when(videoRepository.findByTitleContainingIgnoreCase("Test", pageable)).thenReturn(page);
        when(videoMapper.toDto(video)).thenReturn(videoDto);

        Page<VideoDto> result = videoService.searchVideosByTitle("Test", pageable);

        assertEquals(1, result.getTotalElements());
        verify(videoRepository).findByTitleContainingIgnoreCase("Test", pageable);
    }

    @Test
    @DisplayName("Test searchVideosByTitle empty term")
    void testSearchVideosByTitleEmptyTerm() {
        Page<Video> page = new PageImpl<>(List.of(video));
        when(videoRepository.findAll(pageable)).thenReturn(page);
        when(videoMapper.toDto(video)).thenReturn(videoDto);

        Page<VideoDto> result = videoService.searchVideosByTitle("", pageable);

        assertEquals(1, result.getTotalElements());
        verify(videoRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Test getVideosBySource")
    void testGetVideosBySource() {
        Page<Video> page = new PageImpl<>(List.of(video));
        when(videoRepository.findBySource("youtube", pageable)).thenReturn(page);
        when(videoMapper.toDto(video)).thenReturn(videoDto);

        Page<VideoDto> result = videoService.getVideosBySource("youtube", pageable);

        assertEquals(1, result.getTotalElements());
        verify(videoRepository).findBySource("youtube", pageable);
    }
}
