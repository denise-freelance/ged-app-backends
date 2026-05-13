package com.example.gedappbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponse {
    private UUID id;
    private UUID userId;
    private String userName;
    private String action;
    private String resourceType;
    private UUID resourceId;
    private Map<String, Object> details;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime createdAt;
}
