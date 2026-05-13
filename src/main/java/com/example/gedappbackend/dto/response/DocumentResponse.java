package com.example.gedappbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponse {
    private UUID id;
    private String name;
    private UUID folderId;
    private String folderPath;
    private UUID ownerId;
    private String ownerName;
    private String type;
    private Long size;
    private String mimeType;
    private Integer currentVersion;
    private String status;
    private List<String> tags;
    private boolean isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String previewUrl;
    private List<VersionResponse> versions;
}
