package org.algoteque.coursemanagement.mapper;

import org.algoteque.coursemanagement.domain.Course;
import org.algoteque.coursemanagement.domain.CourseStatus;
import org.algoteque.coursemanagement.dto.CourseRequestDto;
import org.algoteque.coursemanagement.dto.CourseResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public CourseResponseDto toDto(Course course) {
        return CourseResponseDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .duration(course.getDuration())
                .status(course.getStatus())
                .createdAt(course.getCreatedAt())
                .publishedAt(course.getPublishedAt())
                .build();
    }

    public Course toEntity(CourseRequestDto dto) {
        return Course.customBuilder()
                .title(dto.title())
                .description(dto.description())
                .duration(dto.duration())
                .build();
    }
}
