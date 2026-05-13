package com.example.gedappbackend.controller;

import com.example.gedappbackend.dto.request.DocumentUploadRequest;
import com.example.gedappbackend.dto.response.DocumentResponse;
import com.example.gedappbackend.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Tag(name = "Documents", description = "Document management endpoints")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a new document")
    public ResponseEntity<DocumentResponse> uploadDocument(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") @Valid DocumentUploadRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        return ResponseEntity.ok(documentService.uploadDocument(request, file, userId));
    }

    @GetMapping
    @Operation(summary = "Get user's documents")
    public ResponseEntity<Page<DocumentResponse>> getUserDocuments(
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        return ResponseEntity.ok(documentService.getUserDocuments(userId, pageable));
    }

    @GetMapping("/{documentId}")
    @Operation(summary = "Get document by ID")
    public ResponseEntity<DocumentResponse> getDocument(
            @PathVariable UUID documentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        return ResponseEntity.ok(documentService.getDocument(documentId, userId));
    }

    @PutMapping("/{documentId}")
    @Operation(summary = "Update document metadata")
    public ResponseEntity<DocumentResponse> updateDocument(
            @PathVariable UUID documentId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<String> tags,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        return ResponseEntity.ok(documentService.updateDocument(documentId, name, tags, userId));
    }

    @DeleteMapping("/{documentId}")
    @Operation(summary = "Delete document")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable UUID documentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        documentService.deleteDocument(documentId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{documentId}/versions")
    @Operation(summary = "Add new version")
    public ResponseEntity<DocumentResponse> addVersion(
            @PathVariable UUID documentId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String comment,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        return ResponseEntity.ok(documentService.addVersion(documentId, file, comment, userId));
    }
}
