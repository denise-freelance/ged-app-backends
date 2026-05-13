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
public class FolderResponse {
    private UUID id;
    private String name;
    private String path;
    private UUID parentFolderId;
    private UUID ownerId;
    private String ownerName;
    private List<FolderResponse> subFolders;
    private List<DocumentResponse> documents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
