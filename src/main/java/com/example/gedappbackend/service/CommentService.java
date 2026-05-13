package com.example.gedappbackend.service;

import com.example.gedappbackend.model.Comment;
import com.example.gedappbackend.model.Document;
import com.example.gedappbackend.model.NotificationType;
import com.example.gedappbackend.model.User;
import com.example.gedappbackend.repository.CommentRepository;
import com.example.gedappbackend.repository.DocumentRepository;
import com.example.gedappbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final NotificationService notificationService;

    @Transactional
    public CommentResponse addComment(UUID documentId, CommentRequest request, UUID userId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        User author = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Comment comment = Comment.builder()
                .document(document)
                .author(author)
                .content(request.getContent())
                .mentions(request.getMentions())
                .build();

        if (request.getParentCommentId() != null) {
            Comment parent = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found"));
            comment.setParentComment(parent);
        }

        comment = commentRepository.save(comment);

        // Notify mentions
        if (request.getMentions() != null && !request.getMentions().isEmpty()) {
            for (UUID mentionedUserId : request.getMentions()) {
                if (!mentionedUserId.equals(userId)) {
                    notificationService.createNotification(
                            mentionedUserId,
                            NotificationType.MENTION,
                            "Vous avez été mentionné dans un commentaire",
                            String.format("%s vous a mentionné dans le document '%s'",
                                    author.getUsername(), document.getName()),
                            Map.of("documentId", documentId.toString(), "commentId", comment.getId().toString())
                    );
                }
            }
        }

        log.info("Comment added on document {} by user {}", documentId, userId);

        return commentMapper.toResponse(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getDocumentComments(UUID documentId) {
        return commentRepository.findByDocumentIdOrderByCreatedAtAsc(documentId)
                .stream()
                .map(commentMapper::toResponse)
                .toList();
    }
}
