package org.algoteque.coursemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request payload to create or update a course")
public record CourseRequestDto(

        @NotBlank
        @Schema(description = "Title of the course", example = "Introduction to Java")
        String title,

        @Schema(description = "Detailed description of the course", example = "Covers basic Java syntax and OOP")
        String description,

        @Schema(description = "Course duration in hours", example = "40")
        @NotNull(message = "Duration is required")
        @Min(value = 1, message = "Duration must be greater than 0")
        int duration
) {}
