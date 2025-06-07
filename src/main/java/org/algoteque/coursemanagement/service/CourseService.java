package org.algoteque.coursemanagement.service;

import lombok.AllArgsConstructor;
import org.algoteque.coursemanagement.domain.Course;
import org.algoteque.coursemanagement.domain.CourseStatus;
import org.algoteque.coursemanagement.dto.CourseRequestDto;
import org.algoteque.coursemanagement.dto.CourseResponseDto;
import org.algoteque.coursemanagement.exception.CourseNotFoundException;
import org.algoteque.coursemanagement.mapper.CourseMapper;
import org.algoteque.coursemanagement.repository.CourseQueryRepository;
import org.algoteque.coursemanagement.repository.CourseRepository;
import org.algoteque.coursemanagement.validation.CourseValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseService {

    private final CourseRepository repository;
    private final CourseQueryRepository queryRepository;
    private final CourseMapper mapper;
    private final CourseValidator validator;

    public List<CourseResponseDto> findAll() {
        return queryRepository.findAllAsDto();
    }

    public CourseResponseDto findById(Long id) {
        return mapper.toDto(repository.findById(id).orElseThrow(() -> new CourseNotFoundException(id)));
    }

    public CourseResponseDto create(CourseRequestDto dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    public CourseResponseDto update(Long id, CourseRequestDto dto) {
        Course course = repository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(id));

        validator.validateEditable(course);

        course.setTitle(dto.title());
        course.setDescription(dto.description());
        course.setDuration(dto.duration());

        return mapper.toDto(repository.save(course));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public CourseResponseDto publish(Long id) {
        Course course = repository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(id));
        validator.validatePublishable(course);
        course.setStatus(CourseStatus.PUBLISHED);
        course.setPublishedAt(LocalDateTime.now());
        return mapper.toDto(repository.save(course));
    }

    public CourseResponseDto archive(Long id) {
        Course course = repository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(id));
        validator.validateArchivable(course);
        course.setStatus(CourseStatus.ARCHIVED);
        return mapper.toDto(repository.save(course));
    }

    public double getAverageDuration() {
        return Optional.ofNullable(repository.calculateAverageDuration())
                .orElse(0.0);
    }

    public List<CourseResponseDto> findPublishedBetween(String start, String end) {
        LocalDateTime startDate ;
        LocalDateTime endDate;

        try {
            startDate  = LocalDateTime.parse(start);
            endDate = LocalDateTime.parse(end);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected ISO-8601, e.g. 2024-06-01T00:00:00");
        }

        if (startDate .isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must not be after end date.");
        }

        return queryRepository.findPublishedBetween(startDate, endDate);
    }

    public CourseResponseDto duplicate(Long id) {
        Course original = repository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(id));

        Course duplicated = Course.customBuilder()
                .title(original.getTitle())
                .description(original.getDescription())
                .duration(original.getDuration())
                .status(CourseStatus.DRAFT)
                .createdAt(LocalDateTime.now())
                .publishedAt(null)
                .build();

        return mapper.toDto(repository.save(duplicated));
    }



}
