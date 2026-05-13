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
public class VersionResponse {
    private UUID id;
    private Integer versionNumber;
    private String fileUrl;
    private Long fileSize;
    private UUID authorId;
    private String authorName;
    private String comment;
    private LocalDateTime createdAt;
}
