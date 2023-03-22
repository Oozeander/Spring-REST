package com.example.springweb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@JsonPropertyOrder({"timestamp", "status", "error_message", "path"})
public record ErrorResponse(
        LocalDateTime timestamp,
        HttpStatus status,
        @JsonProperty("error_message")
        String errorMessage,
        String path
) {
}
