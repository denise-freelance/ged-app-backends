package com.example.gedappbackend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationRequest {
    @NotNull(message = "Document ID is required")
    private UUID documentId;

    @NotNull(message = "Validator ID is required")
    private UUID validatorId;

    private String message;
    private LocalDateTime dueDate;
}
