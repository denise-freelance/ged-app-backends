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
public class OfflineCacheResponse {
    private UUID id;
    private UUID documentId;
    private String documentName;
    private Integer versionNumber;
    private LocalDateTime syncedAt;
    private boolean isDirty;
    private boolean conflict;
}
