package com.example.gedappbackend.repository;

import com.example.gedappbackend.model.Document;
import com.example.gedappbackend.model.DocumentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {
    Page<Document> findByOwnerId(UUID ownerId, Pageable pageable);
    Page<Document> findByFolderId(UUID folderId, Pageable pageable);
    List<Document> findByStatus(DocumentStatus status);
    Page<Document> findByTagsContaining(String tag, Pageable pageable);

    @Query("SELECT d FROM Document d WHERE " +
            "LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(d.type) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Document> searchDocuments(@Param("search") String search, Pageable pageable);

    @Query("SELECT d FROM Document d WHERE d.owner.id = :userId AND d.status = :status")
    List<Document> findByOwnerAndStatus(@Param("userId") UUID userId, @Param("status") DocumentStatus status);
}
