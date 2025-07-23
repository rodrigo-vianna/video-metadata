package com.goldmediatech.videometadata.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoDto {

    private Long id;
    private String title;
    private String description;
    private String externalId;
    private String source;
    private String thumbnailUrl;
    private String videoUrl;
    private Integer durationSeconds;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime uploadDate;

    private Long viewCount;
    private Long likeCount;
    private String channelName;
    private String channelId;
    private String tags;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    public static VideoDtoBuilder builder() {
        return new VideoDtoBuilder();
    }

    public static class VideoDtoBuilder {
        private VideoDto videoDto;

        public VideoDtoBuilder() {
            this.videoDto = new VideoDto();
        }

        public VideoDtoBuilder id(Long id) {
            videoDto.setId(id);
            return this;
        }

        public VideoDtoBuilder title(String title) {    
            videoDto.setTitle(title);
            return this;
        }

        public VideoDtoBuilder description(String description) {
            videoDto.setDescription(description);
            return this;
        }   

        public VideoDtoBuilder externalId(String externalId) {
            videoDto.setExternalId(externalId);
            return this;
        }

        public VideoDtoBuilder source(String source) {
            videoDto.setSource(source);
            return this;
        }

        public VideoDtoBuilder thumbnailUrl(String thumbnailUrl) {
            videoDto.setThumbnailUrl(thumbnailUrl);
            return this;
        }

        public VideoDtoBuilder videoUrl(String videoUrl) {
            videoDto.setVideoUrl(videoUrl);
            return this;
        }

        public VideoDtoBuilder durationSeconds(Integer durationSeconds) {
            videoDto.setDurationSeconds(durationSeconds);
            return this;
        }

        public VideoDtoBuilder uploadDate(LocalDateTime uploadDate) {
            videoDto.setUploadDate(uploadDate);
            return this;
        }

        public VideoDtoBuilder viewCount(Long viewCount) {
            videoDto.setViewCount(viewCount);
            return this;
        }

        public VideoDtoBuilder likeCount(Long likeCount) {
            videoDto.setLikeCount(likeCount);
            return this;
        }

        public VideoDtoBuilder channelName(String channelName) {
            videoDto.setChannelName(channelName);
            return this;
        }

        public VideoDtoBuilder channelId(String channelId) {
            videoDto.setChannelId(channelId);
            return this;
        }

        public VideoDtoBuilder tags(String tags) {
            videoDto.setTags(tags);
            return this;
        }

        public VideoDtoBuilder createdAt(LocalDateTime createdAt) {
            videoDto.setCreatedAt(createdAt);
            return this;
        }

        public VideoDtoBuilder updatedAt(LocalDateTime updatedAt) {
            videoDto.setUpdatedAt(updatedAt);
            return this;
        }

        public VideoDto build() {
            return videoDto;
        }

    }

}