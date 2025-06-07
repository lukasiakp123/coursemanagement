package org.algoteque.coursemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.algoteque.coursemanagement.domain.CourseStatus;

import java.time.LocalDateTime;

@Schema(description = "Response payload representing a course")
@Builder
public record CourseResponseDto(

        @Schema(description = "Unique identifier of the course", example = "1")
        Long id,

        @Schema(description = "Title of the course", example = "Introduction to Java")
        String title,

        @Schema(description = "Course description", example = "Learn the basics of Java programming")
        String description,

        @Schema(description = "Course duration in hours", example = "40")
        int duration,

        @Schema(description = "Current status of the course", example = "PUBLISHED")
        CourseStatus status,

        @Schema(description = "Timestamp when the course was created")
        LocalDateTime createdAt,

        @Schema(description = "Timestamp when the course was published, if applicable")
        LocalDateTime publishedAt
) {}
