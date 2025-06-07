package org.algoteque.coursemanagement.validation;

import org.algoteque.coursemanagement.domain.Course;
import org.algoteque.coursemanagement.domain.CourseStatus;
import org.springframework.stereotype.Component;

@Component
public class CourseValidator {

    public void validateEditable(Course course) {
        if (course.getStatus() == CourseStatus.ARCHIVED) {
            throw new IllegalStateException("Archived courses cannot be edited.");
        }
    }

    public void validatePublishable(Course course) {
        if (course.getStatus() != CourseStatus.DRAFT) {
            throw new IllegalStateException("Only draft courses can be published.");
        }
        if (course.getTitle() == null || course.getTitle().isBlank()) {
            throw new IllegalStateException("Course must have a title to be published.");
        }
        if (course.getDuration() <= 0) {
            throw new IllegalStateException("Course duration must be greater than zero to be published.");
        }
    }

    public void validateArchivable(Course course) {
        if (course.getStatus() != CourseStatus.PUBLISHED) {
            throw new IllegalStateException("Only published courses can be archived.");
        }
    }
}
