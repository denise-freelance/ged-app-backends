package com.example.gedappbackend.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "validation_workflows")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationWorkflow {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitter_id", nullable = false)
    private User submitter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "validator_id", nullable = false)
    private User validator;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ValidationStatus status = ValidationStatus.PENDING;

    private LocalDate dueDate;

    private String message;

    private LocalDateTime approvedAt;

    private LocalDateTime rejectedAt;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}