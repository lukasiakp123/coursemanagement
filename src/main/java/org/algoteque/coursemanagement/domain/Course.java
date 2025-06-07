package org.algoteque.coursemanagement.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "courses",
        indexes = {
                @Index(
                        name = "idx_course_status_publishedat",
                        columnList = "status, publishedAt"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "customBuilder")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE) //remove setter from @Builder
    @Builder.Default
    private Long id = null;  //remove setter from @Builder

    @NotBlank
    @Column(nullable = false)
    private String title;

    private String description;

    @Min(1)
    @Column(nullable = false)
    private Integer duration;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CourseStatus status = CourseStatus.DRAFT;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime publishedAt;

}
