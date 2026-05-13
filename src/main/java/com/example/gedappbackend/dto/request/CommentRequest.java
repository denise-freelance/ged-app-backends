package com.example.gedappbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    @NotBlank(message = "Comment content is required")
    private String content;

    private UUID parentCommentId;
    private List<UUID> mentions;
}
