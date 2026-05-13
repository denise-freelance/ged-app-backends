package com.example.gedappbackend.repository;

import com.example.gedappbackend.model.ValidationStatus;
import com.example.gedappbackend.model.ValidationWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ValidationWorkflowRepository extends JpaRepository<ValidationWorkflow, UUID> {
    List<ValidationWorkflow> findByValidatorIdAndStatus(UUID validatorId, ValidationStatus status);
    List<ValidationWorkflow> findBySubmitterId(UUID submitterId);
    Optional<ValidationWorkflow> findByDocumentIdAndStatus(UUID documentId, ValidationStatus status);

    @Query("SELECT v FROM ValidationWorkflow v WHERE v.dueDate < :now AND v.status = 'PENDING'")
    List<ValidationWorkflow> findExpiredPendingValidations(@Param("now") LocalDateTime now);
}
