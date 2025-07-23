package com.goldmediatech.videometadata.util;

import com.goldmediatech.videometadata.domain.dto.VideoDto;
import com.goldmediatech.videometadata.domain.entity.Video;
import org.springframework.stereotype.Component;

@Component
public class VideoMapper {

    public VideoDto toDto(Video video) {
        if (video == null) return null;
        return VideoDto.builder()
                .id(video.getId())
                .title(video.getTitle())
                .description(video.getDescription())
                .durationSeconds(video.getDurationSeconds())
                .viewCount(video.getViewCount())
                .likeCount(video.getLikeCount())
                .source(video.getSource())
                .externalId(video.getExternalId())
                .thumbnailUrl(video.getThumbnailUrl())
                .videoUrl(video.getVideoUrl())
                .channelName(video.getChannelName())
                .tags(video.getTags())
                .uploadDate(video.getUploadDate())
                .createdAt(video.getCreatedAt())
                .updatedAt(video.getUpdatedAt())
                .build();
    }

    public Video toEntity(VideoDto dto) {
        if (dto == null) return null;
        Video video = new Video();
        video.setId(dto.getId());
        video.setTitle(dto.getTitle());
        video.setDescription(dto.getDescription());
        video.setDurationSeconds(dto.getDurationSeconds());
        video.setViewCount(dto.getViewCount());
        video.setLikeCount(dto.getLikeCount());
        video.setSource(dto.getSource());
        video.setExternalId(dto.getExternalId());
        video.setThumbnailUrl(dto.getThumbnailUrl());
        video.setVideoUrl(dto.getVideoUrl());
        video.setChannelName(dto.getChannelName());
        video.setTags(dto.getTags());
        video.setUploadDate(dto.getUploadDate());
        video.setCreatedAt(dto.getCreatedAt());
        video.setUpdatedAt(dto.getUpdatedAt());
        return video;
    }
}
