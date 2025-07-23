package com.goldmediatech.videometadata.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "videos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "External ID is required")
    @Column(name = "external_id", unique = true, nullable = false)
    private String externalId;

    @NotBlank(message = "Source is required")
    @Column(nullable = false)
    private String source; // youtube, vimeo, internal

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "video_url")
    private String videoUrl;

    @Positive(message = "Duration must be positive")
    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    @Column(name = "view_count")
    private Long viewCount = 0L;

    @Column(name = "like_count")
    private Long likeCount = 0L;

    @Column(name = "channel_name")
    private String channelName;

    @Column(name = "channel_id")
    private String channelId;

    @Size(max = 1000, message = "Tags cannot exceed 1000 characters")
    private String tags; // Comma-separated tags

    @NotNull(message = "Created at is required")
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @NotNull(message = "Updated at is required")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Video(String title, String externalId, String source) {
        this.title = title;
        this.externalId = externalId;
        this.source = source;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return Objects.equals(id, video.id) && 
               Objects.equals(externalId, video.externalId) && 
               Objects.equals(source, video.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, externalId, source);
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", externalId='" + externalId + '\'' +
                ", source='" + source + '\'' +
                ", durationSeconds=" + durationSeconds +
                ", uploadDate=" + uploadDate +
                ", createdAt=" + createdAt +
                '}';
    }
}