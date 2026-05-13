package com.example.gedappbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "connectors")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Connector {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    @Enumerated(EnumType.STRING)
//    private ConnectorType type;

    @Builder.Default
    private boolean isConnected = false;

    private String credentialsEncrypted;

    private LocalDateTime lastSync;

    @ElementCollection
    @CollectionTable(name = "connector_settings", joinColumns = @JoinColumn(name = "connector_id"))
    @MapKeyColumn(name = "setting_key")
    @Column(name = "setting_value")
    private Map<String, String> settings;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

}
