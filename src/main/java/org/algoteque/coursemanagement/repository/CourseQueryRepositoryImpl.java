package org.algoteque.coursemanagement.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.algoteque.coursemanagement.dto.CourseResponseDto;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@AllArgsConstructor
public class CourseQueryRepositoryImpl implements CourseQueryRepository {

    private EntityManager entityManager;

    private static final String COURSE_DTO_PROJECTION = """
        new org.algoteque.coursemanagement.dto.CourseResponseDto(
            c.id, c.title, c.description, c.duration, c.status, c.createdAt, c.publishedAt
        )
    """;

    @Override
    public List<CourseResponseDto> findAllAsDto() {
        String jpql = "SELECT " + COURSE_DTO_PROJECTION + " FROM Course c";
        return entityManager.createQuery(jpql, CourseResponseDto.class)
                .getResultList();
    }

    @Override
    public List<CourseResponseDto> findPublishedBetween(LocalDateTime start, LocalDateTime end) {
        String jpql = "SELECT " + COURSE_DTO_PROJECTION +
                      " FROM Course c WHERE c.status = 'PUBLISHED' AND c.publishedAt BETWEEN :start AND :end";
        return entityManager.createQuery(jpql, CourseResponseDto.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }
}
