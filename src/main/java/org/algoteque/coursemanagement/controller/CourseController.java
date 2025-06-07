package org.algoteque.coursemanagement.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.algoteque.coursemanagement.dto.CourseRequestDto;
import org.algoteque.coursemanagement.dto.CourseResponseDto;
import org.algoteque.coursemanagement.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@Tag(name = "Course Management", description = "Endpoints for managing courses")
public class CourseController {

    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all courses", description = "Returns a list of all available courses")
    public List<CourseResponseDto> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID", description = "Returns a course with the specified ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course found"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    public CourseResponseDto one(
            @Parameter(description = "ID of the course to retrieve") @PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create new course", description = "Creates a new course from the provided data")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Course created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<CourseResponseDto> create(
            @Valid @RequestBody CourseRequestDto dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update course", description = "Updates an existing course with the given ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course updated successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    public CourseResponseDto update(
            @Parameter(description = "ID of the course to update") @PathVariable Long id,
            @Valid @RequestBody CourseRequestDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete course", description = "Deletes the course with the specified ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Course deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(description = "ID of the course to delete") @PathVariable Long id) {
        service.delete(id);
    }


    @PostMapping("/{id}/publish")
    @Operation(summary = "Publish course", description = "Marks the course as published")
    public CourseResponseDto publish(
            @Parameter(description = "ID of the course to publish") @PathVariable Long id) {
        return service.publish(id);
    }

    @PostMapping("/{id}/archive")
    @Operation(summary = "Archive course", description = "Marks the course as archived")
    public CourseResponseDto archive(
            @Parameter(description = "ID of the course to archive") @PathVariable Long id) {
        return service.archive(id);
    }

    @GetMapping("/average-duration")
    @Operation(summary = "Get average course duration", description = "Returns the average duration of all courses")
    public double getAverageDuration() {
        return service.getAverageDuration();
    }

    @GetMapping("/published")
    @Operation(summary = "Get published courses in date range",
            description = "Returns all published courses within a specified start and end date")
    public List<CourseResponseDto> getPublishedCoursesInRange(
            @Parameter(description = "Start date in format YYYY-MM-DD") @RequestParam String start,
            @Parameter(description = "End date in format YYYY-MM-DD") @RequestParam String end
    ) {
        return service.findPublishedBetween(start, end);
    }

    @PostMapping("/{id}/duplicate")
    @Operation(summary = "Duplicate course", description = "Creates a new course based on an existing one, but resets status to DRAFT")
    public CourseResponseDto duplicate(
            @Parameter(description = "ID of the course to duplicate") @PathVariable Long id) {
        return service.duplicate(id);
    }
}
