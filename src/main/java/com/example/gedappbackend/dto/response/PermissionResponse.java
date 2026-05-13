package com.example.gedappbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponse {
    private UUID id;
    private UUID userId;
    private String userName;
    private UUID groupId;
    private String groupName;
    private UUID resourceId;
    private String resourceType;
    private String permissionLevel;
}
