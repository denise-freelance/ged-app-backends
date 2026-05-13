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
public class ConnectorResponse {
    private UUID id;
    private String type;
    private boolean isConnected;
    private LocalDateTime lastSync;
    private Map<String, String> settings;
}
