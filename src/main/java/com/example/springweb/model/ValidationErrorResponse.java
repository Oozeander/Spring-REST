package com.example.springweb.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@JsonPropertyOrder({"timestamp", "errors", "path"})
public record ValidationErrorResponse(
        LocalDateTime timestamp,
        Map<String, String> errors,
        String path
) {
}
