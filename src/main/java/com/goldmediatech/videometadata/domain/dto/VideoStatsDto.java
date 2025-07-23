package com.goldmediatech.videometadata.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoStatsDto {
    private String source;
    private Long totalVideos;
    private Double averageDuration;
}
