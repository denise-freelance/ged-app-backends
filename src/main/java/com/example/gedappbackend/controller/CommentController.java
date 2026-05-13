package com.example.gedappbackend.controller;

import com.example.gedappbackend.service.CommentService;
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
@RequestMapping("/api/documents/{documentId}/comments")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "Comment management endpoints")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "Add comment to document")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable UUID documentId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        return ResponseEntity.ok(commentService.addComment(documentId, request, userId));
    }

    @GetMapping
    @Operation(summary = "Get document comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable UUID documentId) {
        return ResponseEntity.ok(commentService.getDocumentComments(documentId));
    }
}
