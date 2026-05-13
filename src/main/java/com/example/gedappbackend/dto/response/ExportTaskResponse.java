package com.example.gedappbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportTaskResponse {
    private String taskId;
    private String status;
    private String downloadUrl;
    private Integer totalDocuments;
    private Integer processedDocuments;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}
