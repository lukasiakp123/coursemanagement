package org.algoteque.coursemanagement.repository;

import org.algoteque.coursemanagement.domain.Course;
import org.algoteque.coursemanagement.domain.CourseStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void shouldSaveAndLoadCourse() {
        // given
        Course course = Course.customBuilder()
                .title("Spring Boot")
                .description("Advanced Spring Boot features")
                .duration(40)
                .build();

        // when
        Course saved = courseRepository.save(course);

        // then
        Optional<Course> found = courseRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Spring Boot");
        assertThat(found.get().getDuration()).isEqualTo(40);
        assertThat(found.get().getStatus()).isEqualTo(CourseStatus.DRAFT);
    }
}
