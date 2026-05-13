package com.example.gedappbackend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportRequest {
    @NotEmpty(message = "Document IDs are required")
    private List<UUID> documentIds;

    private boolean includeMetadata;
    private boolean includeVersions;
    private String password;
}
