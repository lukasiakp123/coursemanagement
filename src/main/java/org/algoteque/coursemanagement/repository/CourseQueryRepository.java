package org.algoteque.coursemanagement.repository;

import org.algoteque.coursemanagement.dto.CourseResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface CourseQueryRepository {
    List<CourseResponseDto> findAllAsDto();
    List<CourseResponseDto> findPublishedBetween(LocalDateTime start, LocalDateTime end);
}
