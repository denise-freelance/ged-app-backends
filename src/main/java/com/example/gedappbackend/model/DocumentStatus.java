package com.example.gedappbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

public enum DocumentStatus {
    DRAFT,      // Brouillon
    PENDING,    // En attente de validation
    APPROVED,   // Validé
    REJECTED,   // Rejeté
    ARCHIVED    // Archivé
}