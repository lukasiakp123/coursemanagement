package org.algoteque.coursemanagement.repository;

import org.algoteque.coursemanagement.domain.Course;
import org.algoteque.coursemanagement.domain.CourseStatus;
import org.algoteque.coursemanagement.dto.CourseResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CourseQueryRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    private final CourseQueryRepository queryRepository;

    @Autowired
    public CourseQueryRepositoryTest(EntityManager entityManager) {
        this.queryRepository = new CourseQueryRepositoryImpl(entityManager);
    }

    @Test
    @DisplayName("Should return only published courses within the specified date range")
    void shouldReturnPublishedCoursesInDateRange() {
        // given
        Course published = Course.customBuilder()
                .title("Spring Boot")
                .description("REST API course")
                .duration(10)
                .status(CourseStatus.PUBLISHED)
                .publishedAt(LocalDateTime.now().minusDays(1))
                .build();

        Course draft = Course.customBuilder()
                .title("Draft Course")
                .description("This is a draft")
                .duration(5)
                .status(CourseStatus.DRAFT)
                .build();

        courseRepository.saveAll(List.of(published, draft));

        // when
        LocalDateTime start = LocalDateTime.now().minusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        List<CourseResponseDto> result = queryRepository.findPublishedBetween(start, end);

        // then
        assertThat(result).hasSize(1);
        CourseResponseDto course = result.get(0);
        assertThat(course.title()).isEqualTo("Spring Boot");
        assertThat(course.status()).isEqualTo(CourseStatus.PUBLISHED);
    }
}
