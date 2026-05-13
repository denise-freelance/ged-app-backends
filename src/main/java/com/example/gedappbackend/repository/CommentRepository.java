package com.example.gedappbackend.repository;

import com.example.gedappbackend.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByDocumentIdOrderByCreatedAtAsc(UUID documentId);
    List<Comment> findByMentionsContaining(UUID userId);
    Page<Comment> findByAuthorId(UUID authorId, Pageable pageable);
}
