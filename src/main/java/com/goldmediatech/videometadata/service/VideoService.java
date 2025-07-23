package com.goldmediatech.videometadata.service;

import com.goldmediatech.videometadata.domain.dto.VideoDto;
import com.goldmediatech.videometadata.domain.dto.VideoStatsDto;
import com.goldmediatech.videometadata.domain.entity.Video;
import com.goldmediatech.videometadata.persistence.repository.VideoRepository;
import com.goldmediatech.videometadata.util.VideoMapper;
import com.goldmediatech.videometadata.web.exception.ResourceNotFoundException;
import com.goldmediatech.videometadata.web.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VideoService {

    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;
    private final ExternalApiService externalApiService;

    public Page<VideoDto> getAllVideos(String source, Integer minDuration, Integer maxDuration,
            LocalDateTime uploadDate,
            Pageable pageable) {

        Page<Video> videos;

        if (hasFilters(source, minDuration, maxDuration, uploadDate)) {
            videos = videoRepository.findByFilters(source, minDuration, maxDuration, uploadDate, pageable);
        } else {
            videos = videoRepository.findAll(pageable);
        }

        return videos.map(videoMapper::toDto);
    }

    public VideoDto getVideoById(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with ID: " + id));
        return videoMapper.toDto(video);
    }

    @Transactional
    public VideoDto createVideo(VideoDto videoDto) {

        // Check for duplicates based on external_id and source
        if (StringUtils.hasText(videoDto.getExternalId()) &&
                StringUtils.hasText(videoDto.getSource())) {

            Optional<Video> existingVideo = videoRepository
                    .findByExternalIdAndSource(videoDto.getExternalId(), videoDto.getSource());

            if (existingVideo.isPresent()) {
                throw new DuplicateResourceException(
                        "Video already exists with external_id: " + videoDto.getExternalId() +
                                " and source: " + videoDto.getSource());
            }
        }

        Video video = videoMapper.toEntity(videoDto);
        video.setCreatedAt(LocalDateTime.now());
        video.setUpdatedAt(LocalDateTime.now());

        Video savedVideo = videoRepository.save(video);

        return videoMapper.toDto(savedVideo);
    }

    @Transactional
    public void deleteVideo(Long id) {
        if (!videoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Video not found with ID: " + id);
        }

        videoRepository.deleteById(id);
    }

    @Transactional
    public List<VideoDto> importVideosFromExternalApi(String source) {

        if (!isValidSource(source)) {
            throw new IllegalArgumentException("Invalid source: " + source);
        }

        try {
            List<VideoDto> externalVideos = externalApiService.fetchVideos(source);

            List<VideoDto> importedVideos = externalVideos.stream()
                    .map(this::importVideoIfNotExists)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();

            return importedVideos;

        } catch (Exception e) {
            log.error("Error importing videos from external API: {}", source, e);
            throw new RuntimeException("Failed to import videos from " + source + ": " + e.getMessage());
        }
    }

    public List<VideoStatsDto> getVideoStatistics() {
        List<Object[]> rawStats = videoRepository.getStatisticsBySource();

        List<VideoStatsDto> stats = rawStats.stream()
                .<VideoStatsDto>map(this::mapToVideoStatsDto)
                .toList();

        return stats;
    }

    public Page<VideoDto> searchVideosByTitle(String searchTerm, Pageable pageable) {
        if (!StringUtils.hasText(searchTerm)) {
            return getAllVideos(null, null, null, null, pageable);
        }

        Page<Video> videos = videoRepository.findByTitleContainingIgnoreCase(searchTerm, pageable);

        return videos.map(videoMapper::toDto);
    }

    public Page<VideoDto> getVideosBySource(String source, Pageable pageable) {
        Page<Video> videos = videoRepository.findBySource(source, pageable);

        return videos.map(videoMapper::toDto);
    }

    private boolean hasFilters(String source, Integer minDuration, Integer maxDuration,
            LocalDateTime uploadDate) {
        return StringUtils.hasText(source) || minDuration != null || maxDuration != null ||
                uploadDate != null;
    }

    private Optional<VideoDto> importVideoIfNotExists(VideoDto videoDto) {
        try {
            if (StringUtils.hasText(videoDto.getExternalId()) &&
                    StringUtils.hasText(videoDto.getSource())) {

                Optional<Video> existing = videoRepository
                        .findByExternalIdAndSource(videoDto.getExternalId(), videoDto.getSource());

                if (existing.isPresent()) {
                    return Optional.empty();
                }
            }

            VideoDto imported = createVideo(videoDto);
            return Optional.of(imported);

        } catch (Exception e) {
            log.warn("Failed to import video: {} - Error: {}",
                    videoDto.getTitle(), e.getMessage());
            return Optional.empty();
        }
    }

    private boolean isValidSource(String source) {
        return StringUtils.hasText(source) &&
                List.of("youtube", "vimeo", "internal").contains(source.toLowerCase());
    }

    private VideoStatsDto mapToVideoStatsDto(Object[] rawStats) {
        return VideoStatsDto.builder()
                .source((String) rawStats[0])
                .totalVideos(((Number) rawStats[1]).longValue())
                .averageDuration(((Number) rawStats[2]).doubleValue())
                .build();
    }

}
