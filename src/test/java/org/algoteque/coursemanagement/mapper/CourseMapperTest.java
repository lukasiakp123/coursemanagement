package org.algoteque.coursemanagement.mapper;

import org.algoteque.coursemanagement.domain.Course;
import org.algoteque.coursemanagement.domain.CourseStatus;
import org.algoteque.coursemanagement.dto.CourseRequestDto;
import org.algoteque.coursemanagement.dto.CourseResponseDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CourseMapperTest {

    private final CourseMapper mapper = new CourseMapper();

    @Test
    void shouldMapCourseToDto() {
        // given
        Course course = Course.customBuilder()
            .title("Java")
            .description("Learn Java")
            .duration(10)
            .status(CourseStatus.PUBLISHED)
            .createdAt(LocalDateTime.now())
            .publishedAt(LocalDateTime.now())
            .build();

        // when
        CourseResponseDto dto = mapper.toDto(course);

        // then
        assertThat(dto.title()).isEqualTo("Java");
        assertThat(dto.status()).isEqualTo(CourseStatus.PUBLISHED);
    }

    @Test
    void shouldMapDtoToCourse() {
        // given
        CourseRequestDto requestDto = new CourseRequestDto("Spring", "REST APIs", 15);

        // when
        Course entity = mapper.toEntity(requestDto);

        // then
        assertThat(entity.getTitle()).isEqualTo("Spring");
        assertThat(entity.getDuration()).isEqualTo(15);
    }
}
