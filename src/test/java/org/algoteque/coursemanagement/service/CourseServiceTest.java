package org.algoteque.coursemanagement.service;

import org.algoteque.coursemanagement.domain.Course;
import org.algoteque.coursemanagement.domain.CourseStatus;
import org.algoteque.coursemanagement.dto.CourseRequestDto;
import org.algoteque.coursemanagement.dto.CourseResponseDto;
import org.algoteque.coursemanagement.exception.CourseNotFoundException;
import org.algoteque.coursemanagement.mapper.CourseMapper;
import org.algoteque.coursemanagement.repository.CourseQueryRepository;
import org.algoteque.coursemanagement.repository.CourseRepository;
import org.algoteque.coursemanagement.validation.CourseValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @Mock
    private CourseRepository repository;

    @Mock
    private CourseQueryRepository queryRepository;

    @Mock
    private CourseMapper mapper;

    @Mock
    private CourseValidator validator;

    @InjectMocks
    private CourseService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnCourseById() {
        Course course = Course.customBuilder()
                .title("Title")
                .description("Desc")
                .duration(5)
                .status(CourseStatus.DRAFT)
                .createdAt(LocalDateTime.now())
                .build();

        CourseResponseDto dto = CourseResponseDto.builder()
                .id(1L)
                .title("Title")
                .description("Desc")
                .duration(5)
                .status(CourseStatus.DRAFT)
                .createdAt(course.getCreatedAt())
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(course));
        when(mapper.toDto(course)).thenReturn(dto);

        CourseResponseDto result = service.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    void shouldThrowWhenCourseNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findById(1L))
                .isInstanceOf(CourseNotFoundException.class);
    }

    @Test
    void shouldCreateCourse() {
        CourseRequestDto request = new CourseRequestDto("Title", "Desc", 5);

        Course course = Course.customBuilder()
                .title("Title")
                .description("Desc")
                .duration(5)
                .status(CourseStatus.DRAFT)
                .createdAt(LocalDateTime.now())
                .build();

        CourseResponseDto response = CourseResponseDto.builder()
                .id(1L)
                .title("Title")
                .description("Desc")
                .duration(5)
                .status(CourseStatus.DRAFT)
                .createdAt(course.getCreatedAt())
                .build();

        when(mapper.toEntity(request)).thenReturn(course);
        when(repository.save(course)).thenReturn(course);
        when(mapper.toDto(course)).thenReturn(response);

        CourseResponseDto result = service.create(request);

        assertThat(result.title()).isEqualTo("Title");
    }

    @Test
    void shouldPublishCourse() {
        Course draft = Course.customBuilder()
                .title("Title")
                .description("Desc")
                .duration(5)
                .status(CourseStatus.DRAFT)
                .createdAt(LocalDateTime.now())
                .build();

        Course published = Course.customBuilder()
                .title("Title")
                .description("Desc")
                .duration(5)
                .status(CourseStatus.PUBLISHED)
                .createdAt(draft.getCreatedAt())
                .publishedAt(LocalDateTime.now())
                .build();

        CourseResponseDto dto = CourseResponseDto.builder()
                .id(1L)
                .title("Title")
                .description("Desc")
                .duration(5)
                .status(CourseStatus.PUBLISHED)
                .createdAt(published.getCreatedAt())
                .publishedAt(published.getPublishedAt())
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(draft));
        doNothing().when(validator).validatePublishable(draft);
        when(repository.save(any(Course.class))).thenReturn(published);
        when(mapper.toDto(published)).thenReturn(dto);

        CourseResponseDto result = service.publish(1L);

        assertThat(result.status()).isEqualTo(CourseStatus.PUBLISHED);
    }

    @Test
    void shouldReturnAverageDuration() {
        when(repository.calculateAverageDuration()).thenReturn(10.5);
        assertThat(service.getAverageDuration()).isEqualTo(10.5);
    }

    @Test
    void shouldReturnZeroIfAverageIsNull() {
        when(repository.calculateAverageDuration()).thenReturn(null);
        assertThat(service.getAverageDuration()).isEqualTo(0.0);
    }

    @Test
    void shouldDuplicateCourse() {
        Course original = Course.customBuilder()
                .id(1L)
                .title("Original")
                .description("Desc")
                .duration(30)
                .status(CourseStatus.PUBLISHED)
                .createdAt(LocalDateTime.of(2024, 1, 1, 10, 0))
                .publishedAt(LocalDateTime.of(2024, 1, 2, 10, 0))
                .build();

        Course savedDuplicated = Course.customBuilder()
                .id(2L)
                .title("Original")
                .description("Desc")
                .duration(30)
                .status(CourseStatus.DRAFT)
                .createdAt(LocalDateTime.now())
                .build();

        CourseResponseDto expectedDto = CourseResponseDto.builder()
                .id(2L)
                .title("Original")
                .description("Desc")
                .duration(30)
                .status(CourseStatus.DRAFT)
                .createdAt(savedDuplicated.getCreatedAt())
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(original));
        when(repository.save(any(Course.class))).thenReturn(savedDuplicated);
        when(mapper.toDto(savedDuplicated)).thenReturn(expectedDto);

        CourseResponseDto result = service.duplicate(1L);

        assertThat(result.id()).isEqualTo(2L);
        assertThat(result.title()).isEqualTo("Original");
        assertThat(result.status()).isEqualTo(CourseStatus.DRAFT);
        assertThat(result.publishedAt()).isNull();
    }

    @Test
    void shouldThrowWhenDuplicatedCourseNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.duplicate(999L))
                .isInstanceOf(CourseNotFoundException.class);
    }

}
