package com.example.gedappbackend.repository;

import com.example.gedappbackend.model.OfflineCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OfflineCacheRepository extends JpaRepository<OfflineCache, UUID> {
    List<OfflineCache> findByUserId(UUID userId);
    Optional<OfflineCache> findByUserIdAndDocumentId(UUID userId, UUID documentId);
    List<OfflineCache> findByUserIdAndIsDirtyTrue(UUID userId);
    void deleteByUserId(UUID userId);
}
