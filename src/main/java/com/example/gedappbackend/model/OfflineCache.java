package com.example.gedappbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "offline_cache")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfflineCache {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    private Integer versionNumber;

    private LocalDateTime syncedAt;

    @Builder.Default
    private boolean isDirty = false;

    private String localHash;

    @Builder.Default
    private boolean conflict = false;
}
