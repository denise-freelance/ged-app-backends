package com.example.gedappbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class DocumentVersion {
    @Id
    private UUID id;
    private Integer versionNumber;
    private String storagePath;
    private Long size;
    private String changeComment;

    @ManyToOne
    private User createdBy;

    @ManyToOne
    private Document document;

    private LocalDateTime createdAt;
}