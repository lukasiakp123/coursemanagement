package org.algoteque.coursemanagement.dto;

import java.util.List;

public record PagedResponseDto<T>(
        List<T> content,
        int number,
        int size,
        long totalElements,
        int totalPages
) {}
