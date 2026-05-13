package com.example.gedappbackend.repository;

import com.example.gedappbackend.model.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VersionRepository extends JpaRepository<Version, UUID> {
    List<Version> findByDocumentIdOrderByVersionNumberDesc(UUID documentId);
    Optional<Version> findByDocumentIdAndVersionNumber(UUID documentId, Integer versionNumber);
    void deleteByDocumentId(UUID documentId);
}
