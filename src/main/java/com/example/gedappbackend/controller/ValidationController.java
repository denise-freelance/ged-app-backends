package com.example.gedappbackend.controller;

import com.example.gedappbackend.dto.request.ValidationRequest;
import com.example.gedappbackend.dto.response.ValidationWorkflowResponse;
import com.example.gedappbackend.service.ValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/validation")
@RequiredArgsConstructor
@Tag(name = "Validation", description = "Document validation workflow endpoints")
public class ValidationController {

    private final ValidationService validationService;

    @PostMapping("/submit")
    @Operation(summary = "Submit document for validation")
    public ResponseEntity<ValidationWorkflowResponse> submitForValidation(
            @Valid @RequestBody ValidationRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID submitterId = UUID.fromString(userDetails.getUsername());
        return ResponseEntity.ok(validationService.submitForValidation(
                request.getDocumentId(),
                request.getValidatorId(),
                request.getMessage(),
                request.getDueDate(),
                submitterId
        ));
    }

    @PostMapping("/{workflowId}/approve")
    @Operation(summary = "Approve document")
    public ResponseEntity<ValidationWorkflowResponse> approve(
            @PathVariable UUID workflowId,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID validatorId = UUID.fromString(userDetails.getUsername());
        return ResponseEntity.ok(validationService.approveDocument(workflowId, validatorId));
    }

    @PostMapping("/{workflowId}/reject")
    @Operation(summary = "Reject document")
    public ResponseEntity<ValidationWorkflowResponse> reject(
            @PathVariable UUID workflowId,
            @RequestParam String reason,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID validatorId = UUID.fromString(userDetails.getUsername());
        return ResponseEntity.ok(validationService.rejectDocument(workflowId, validatorId, reason));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending validations")
    public ResponseEntity<List<ValidationWorkflowResponse>> getPendingValidations(
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID validatorId = UUID.fromString(userDetails.getUsername());
        return ResponseEntity.ok(validationService.getPendingValidations(validatorId));
    }
}
