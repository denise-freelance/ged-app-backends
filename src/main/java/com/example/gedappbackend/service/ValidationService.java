package com.example.gedappbackend.service;

import com.example.gedappbackend.model.*;
import com.example.gedappbackend.repository.DocumentRepository;
import com.example.gedappbackend.repository.UserRepository;
import com.example.gedappbackend.repository.ValidationWorkflowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ValidationService {

    private final ValidationWorkflowRepository validationWorkflowRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final ValidationWorkflowMapper validationWorkflowMapper;
    private final NotificationService notificationService;

    @Transactional
    public ValidationWorkflowResponse submitForValidation(UUID documentId, UUID validatorId,
                                                          String message, LocalDateTime dueDate,
                                                          UUID submitterId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        User validator = userRepository.findById(validatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Validator not found"));

        User submitter = userRepository.findById(submitterId)
                .orElseThrow(() -> new ResourceNotFoundException("Submitter not found"));

        // Check if already in validation
        if (document.getStatus() == DocumentStatus.PENDING) {
            throw new ValidationException("Document already in validation workflow");
        }

        ValidationWorkflow workflow = ValidationWorkflow.builder()
                .document(document)
                .submitter(submitter)
                .validator(validator)
                .dueDate(dueDate != null ? dueDate.toLocalDate() : null)
                .message(message)
                .status(ValidationStatus.PENDING)
                .build();

        workflow = validationWorkflowRepository.save(workflow);

        // Update document status
        document.setStatus(DocumentStatus.PENDING);
        documentRepository.save(document);

        // Notify validator
        notificationService.createNotification(
                validatorId,
                NotificationType.VALIDATION_REQUEST,
                "Document à valider",
                String.format("%s vous demande de valider le document '%s'",
                        submitter.getUsername(), document.getName()),
                Map.of("documentId", documentId.toString(), "workflowId", workflow.getId().toString())
        );

        log.info("Document {} submitted for validation to {}", documentId, validatorId);

        return validationWorkflowMapper.toResponse(workflow);
    }

    @Transactional
    public ValidationWorkflowResponse approveDocument(UUID workflowId, UUID validatorId) {
        ValidationWorkflow workflow = validationWorkflowRepository.findById(workflowId)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow not found"));

        if (!workflow.getValidator().getId().equals(validatorId)) {
            throw new ValidationException("You are not the validator for this document");
        }

        if (workflow.getStatus() != ValidationStatus.PENDING) {
            throw new ValidationException("This workflow is already processed");
        }

        workflow.setStatus(ValidationStatus.APPROVED);
        workflow.setApprovedAt(LocalDateTime.now());
        workflow = validationWorkflowRepository.save(workflow);

        // Update document status
        Document document = workflow.getDocument();
        document.setStatus(DocumentStatus.APPROVED);
        documentRepository.save(document);

        // Notify submitter
        notificationService.createNotification(
                workflow.getSubmitter().getId(),
                NotificationType.VALIDATION_APPROVED,
                "Document approuvé",
                String.format("Le document '%s' a été approuvé par %s",
                        document.getName(), workflow.getValidator().getUsername()),
                Map.of("documentId", document.getId().toString())
        );

        log.info("Document {} approved by {}", document.getId(), validatorId);

        return validationWorkflowMapper.toResponse(workflow);
    }

    @Transactional
    public ValidationWorkflowResponse rejectDocument(UUID workflowId, UUID validatorId, String reason) {
        ValidationWorkflow workflow = validationWorkflowRepository.findById(workflowId)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow not found"));

        if (!workflow.getValidator().getId().equals(validatorId)) {
            throw new ValidationException("You are not the validator for this document");
        }

        if (workflow.getStatus() != ValidationStatus.PENDING) {
            throw new ValidationException("This workflow is already processed");
        }

        workflow.setStatus(ValidationStatus.REJECTED);
        workflow.setRejectedAt(LocalDateTime.now());
        workflow.setMessage(reason);
        workflow = validationWorkflowRepository.save(workflow);

        // Update document status
        Document document = workflow.getDocument();
        document.setStatus(DocumentStatus.REJECTED);
        documentRepository.save(document);

        // Notify submitter
        notificationService.createNotification(
                workflow.getSubmitter().getId(),
                NotificationType.VALIDATION_REJECTED,
                "Document rejeté",
                String.format("Le document '%s' a été rejeté par %s. Raison: %s",
                        document.getName(), workflow.getValidator().getUsername(), reason),
                Map.of("documentId", document.getId().toString())
        );

        log.info("Document {} rejected by {}", document.getId(), validatorId);

        return validationWorkflowMapper.toResponse(workflow);
    }

    @Transactional(readOnly = true)
    public List<ValidationWorkflowResponse> getPendingValidations(UUID validatorId) {
        return validationWorkflowRepository.findByValidatorIdAndStatus(validatorId, ValidationStatus.PENDING)
                .stream()
                .map(validationWorkflowMapper::toResponse)
                .toList();
    }
}
