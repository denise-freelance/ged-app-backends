package com.example.gedappbackend.dto.response;

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
public class ValidationWorkflowResponse {
    private UUID id;
    private UUID documentId;
    private String documentName;
    private UUID submitterId;
    private String submitterName;
    private UUID validatorId;
    private String validatorName;
    private String status;
    private LocalDateTime dueDate;
    private String message;
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;
    private LocalDateTime createdAt;
}
