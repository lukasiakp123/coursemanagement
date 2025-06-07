package org.algoteque.coursemanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.algoteque.coursemanagement.domain.Course;
import org.algoteque.coursemanagement.domain.CourseStatus;
import org.algoteque.coursemanagement.dto.CourseRequestDto;
import org.algoteque.coursemanagement.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CourseRepository repository;

    private Course draftCourse;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        draftCourse = Course.customBuilder()
                .title("Java Basics")
                .description("Intro to Java")
                .duration(40)
                .status(CourseStatus.DRAFT)
                .createdAt(LocalDateTime.now())
                .build();
        repository.save(draftCourse);
    }

    @Test
    void shouldGetAllCourses() throws Exception {
        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Java Basics"));
    }

    @Test
    void shouldGetCourseById() throws Exception {
        mockMvc.perform(get("/api/courses/" + draftCourse.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(draftCourse.getId()));
    }

    @Test
    void shouldReturnNotFoundForInvalidId() throws Exception {
        mockMvc.perform(get("/api/courses/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateCourse() throws Exception {
        CourseRequestDto dto = new CourseRequestDto("Spring Boot", "Spring Course", 30);

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Spring Boot"));
    }

    @Test
    void shouldUpdateCourse() throws Exception {
        CourseRequestDto updateDto = new CourseRequestDto("Updated Title", "Updated Desc", 45);

        mockMvc.perform(put("/api/courses/" + draftCourse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void shouldDeleteCourse() throws Exception {
        mockMvc.perform(delete("/api/courses/" + draftCourse.getId()))
                .andExpect(status().isNoContent());

        assertThat(repository.findById(draftCourse.getId())).isEmpty();
    }

    @Test
    void shouldPublishCourse() throws Exception {
        mockMvc.perform(post("/api/courses/" + draftCourse.getId() + "/publish"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PUBLISHED"));
    }

    @Test
    void shouldArchiveCourse() throws Exception {
        draftCourse.setStatus(CourseStatus.PUBLISHED);
        draftCourse.setPublishedAt(LocalDateTime.now());
        repository.save(draftCourse);

        mockMvc.perform(post("/api/courses/" + draftCourse.getId() + "/archive"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ARCHIVED"));
    }

    @Test
    void shouldReturnAverageDuration() throws Exception {
        mockMvc.perform(get("/api/courses/average-duration"))
                .andExpect(status().isOk())
                .andExpect(content().string("40.0"));
    }

    @Test
    void shouldFindPublishedBetweenDates() throws Exception {
        draftCourse.setStatus(CourseStatus.PUBLISHED);
        draftCourse.setPublishedAt(LocalDateTime.now().minusDays(1));
        repository.save(draftCourse);

        String start = LocalDateTime.now().minusDays(2).toString();
        String end = LocalDateTime.now().plusDays(1).toString();

        mockMvc.perform(get("/api/courses/published")
                        .param("start", start)
                        .param("end", end))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PUBLISHED"));
    }

    @Test
    void shouldValidateInvalidRequest() throws Exception {
        CourseRequestDto invalid = new CourseRequestDto("", "", -5);

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDuplicateCourse() throws Exception {
        Course course = Course.customBuilder()
                .title("To Duplicate")
                .description("Duplicate description")
                .duration(25)
                .status(CourseStatus.PUBLISHED)
                .createdAt(LocalDateTime.now())
                .publishedAt(LocalDateTime.now())
                .build();
        repository.save(course);

        mockMvc.perform(post("/api/courses/" + course.getId() + "/duplicate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("To Duplicate"))
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.publishedAt").doesNotExist());
    }

    @Test
    void shouldReturnCoursesWithStatusFilter() throws Exception {
        repository.deleteAll();

        Course draft = repository.save(Course.customBuilder()
                .title("Draft Course").description("desc").duration(5)
                .status(CourseStatus.DRAFT).createdAt(LocalDateTime.now()).build());

        Course published = repository.save(Course.customBuilder()
                .title("Published Course").description("desc").duration(5)
                .status(CourseStatus.PUBLISHED).createdAt(LocalDateTime.now()).build());

        mockMvc.perform(get("/api/courses")
                        .param("status", "DRAFT")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title").value("Draft Course"))
                .andExpect(jsonPath("$.content[0].status").value("DRAFT"));
    }

    @Test
    void shouldReturnPagedCourses() throws Exception {
        repository.deleteAll();

        for (int i = 1; i <= 15; i++) {
            repository.save(Course.customBuilder()
                    .title("Course " + i).description("desc").duration(5)
                    .status(CourseStatus.DRAFT).createdAt(LocalDateTime.now()).build());
        }

        mockMvc.perform(get("/api/courses")
                        .param("page", "0")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(5)))
                .andExpect(jsonPath("$.totalElements").value(15))
                .andExpect(jsonPath("$.totalPages").value(3));
    }

    @Test
    void shouldReturnEmptyListWhenNoCourses() throws Exception {
        repository.deleteAll();

        mockMvc.perform(get("/api/courses")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0));
    }

    @Test
    void shouldReturnBadRequestForInvalidStatus() throws Exception {
        mockMvc.perform(get("/api/courses")
                        .param("status", "INVALID_STATUS")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestForNegativePage() throws Exception {
        mockMvc.perform(get("/api/courses")
                        .param("page", "-1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
