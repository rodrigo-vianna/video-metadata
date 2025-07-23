package com.goldmediatech.videometadata.persistence.repository;

import com.goldmediatech.videometadata.domain.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long>, JpaSpecificationExecutor<Video> {
        Optional<Video> findByExternalIdAndSource(String externalId, String source);

        Page<Video> findBySource(String source, Pageable pageable);

        Page<Video> findByUploadDateAfter(LocalDateTime uploadDate, Pageable pageable);

        Page<Video> findByDurationSecondsBetween(Integer minDuration, Integer maxDuration, Pageable pageable);

        @Query("SELECT v.source as source, COUNT(v) as count FROM Video v GROUP BY v.source")
        List<Object[]> countVideosBySource();

        @Query("SELECT v.source as source, AVG(v.durationSeconds) as avgDuration FROM Video v " +
                        "WHERE v.durationSeconds IS NOT NULL GROUP BY v.source")
        List<Object[]> averageDurationBySource();

        @Query("SELECT AVG(v.durationSeconds) FROM Video v WHERE v.durationSeconds IS NOT NULL")
        Double getOverallAverageDuration();

        boolean existsByExternalIdAndSource(String externalId, String source);

        Page<Video> findBySourceAndChannelId(String source, String channelId, Pageable pageable);

        Page<Video> findAllByOrderByViewCountDesc(Pageable pageable);

        Page<Video> findAllByOrderByUploadDateDesc(Pageable pageable);

        @Query("SELECT v FROM Video v WHERE " +
                        "(:source IS NULL OR v.source = :source) AND " +
                        "(:minDuration IS NULL OR v.durationSeconds >= :minDuration) AND " +
                        "(:maxDuration IS NULL OR v.durationSeconds <= :maxDuration) AND " +
                        "(:uploadDate IS NULL OR v.uploadDate >= :uploadDate)")
        Page<Video> findByFilters(String source, Integer minDuration, Integer maxDuration, LocalDateTime uploadDate,
                        Pageable pageable);

        @Query("SELECT v.source as source, COUNT(v) as count, AVG(v.durationSeconds) as avgDuration FROM Video v GROUP BY v.source")
        List<Object[]> getStatisticsBySource();

        @Query("SELECT v FROM Video v WHERE LOWER(v.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
        Page<Video> findByTitleContainingIgnoreCase(String searchTerm, Pageable pageable);

}