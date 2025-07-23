package com.goldmediatech.videometadata.web.controller;

import com.goldmediatech.videometadata.domain.dto.VideoDto;
import com.goldmediatech.videometadata.domain.dto.VideoStatsDto;
import com.goldmediatech.videometadata.service.VideoService;
import com.goldmediatech.videometadata.service.ExternalApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/videos")
@Tag(name = "Video Management", description = "Operations for managing video metadata from multiple platforms")
@SecurityRequirement(name = "Bearer Authentication")
public class VideoController {

        private static final Logger logger = LoggerFactory.getLogger(VideoController.class);

        private final VideoService videoService;
        private final ExternalApiService externalApiService;

        public VideoController(VideoService videoService, ExternalApiService externalApiService) {
                this.videoService = videoService;
                this.externalApiService = externalApiService;
        }

        @PostMapping("/import")
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(summary = "Import video metadata from external APIs", description = "Imports video metadata from specified external video platforms. Only accessible by administrators.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Videos imported successfully", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "403", description = "Access denied - Admin role required", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "400", description = "Invalid source parameter", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "500", description = "Import operation failed", content = @Content(mediaType = "application/json"))
        })
        public ResponseEntity<?> importVideos(
                        @Parameter(description = "Video source platform", example = "youtube") @RequestParam(required = false) String source) {

                logger.info("Import videos request received for source: {}", source);

                try {
                        List<VideoDto> importedVideos = this.externalApiService.fetchVideos(source);

                        logger.info("Successfully imported {} videos from source: {}", importedVideos.size(), source);

                        return ResponseEntity.ok()
                                        .body(String.format(
                                                        "{\"message\": \"Successfully imported %d videos from %s\", \"count\": %d}",
                                                        importedVideos.size(), source != null ? source : "all sources",
                                                        importedVideos.size()));

                } catch (Exception e) {
                        logger.error("Failed to import videos from source: {} - {}", source, e.getMessage());
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(String.format("{\"error\": \"Import failed: %s\"}", e.getMessage()));
                }
        }

        @GetMapping
        @Operation(summary = "List all videos with optional filters", description = "Retrieves a paginated list of videos with optional filtering by source, upload date, and duration")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Videos retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized - Valid JWT token required", content = @Content(mediaType = "application/json"))
        })
        public ResponseEntity<Page<VideoDto>> getAllVideos(
                        @Parameter(description = "Filter by video source platform") @RequestParam(required = false) String source,
                        @Parameter(description = "Filter by minimum duration in seconds") @RequestParam(required = false) Integer minDuration,
                        @Parameter(description = "Filter by maximum duration in seconds") @RequestParam(required = false) Integer maxDuration,

                        @Parameter(description = "Filter by upload date (ISO format: yyyy-MM-dd'T'HH:mm:ss)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime uploadDate,


                        Pageable pageable) {
                Page<VideoDto> videos = videoService.getAllVideos(source, minDuration, maxDuration, uploadDate, pageable);

                logger.info("Retrieved {} videos (page {} of {})", videos.getNumberOfElements(),
                                videos.getNumber() + 1, videos.getTotalPages());

                return ResponseEntity.ok(videos);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get video by ID", description = "Retrieves detailed information about a specific video by its unique identifier")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Video found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VideoDto.class))),
                        @ApiResponse(responseCode = "404", description = "Video not found", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "401", description = "Unauthorized - Valid JWT token required", content = @Content(mediaType = "application/json"))
        })
        public ResponseEntity<VideoDto> getVideoById(
                        @Parameter(description = "Video unique identifier", example = "1") @PathVariable Long id) {
                VideoDto video = videoService.getVideoById(id);

                logger.info("Retrieved video: {} (ID: {})", video.getTitle(), id);

                return ResponseEntity.ok(video);
        }

        @GetMapping("/stats")
        @Operation(summary = "Get video statistics", description = "Returns statistical information about videos including total count per source and average duration per source")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VideoStatsDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized - Valid JWT token required", content = @Content(mediaType = "application/json"))
        })
        public ResponseEntity<List<VideoStatsDto>> getVideoStats() {
                List<VideoStatsDto> stats = videoService.getVideoStatistics();
                return ResponseEntity.ok(stats);
        }
}