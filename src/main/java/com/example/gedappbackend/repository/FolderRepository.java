package com.example.gedappbackend.repository;

import com.example.gedappbackend.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FolderRepository extends JpaRepository<Folder, UUID> {
    List<Folder> findByOwnerId(UUID ownerId);
    List<Folder> findByParentFolderId(UUID parentFolderId);
    Optional<Folder> findByPath(String path);
    boolean existsByNameAndParentFolderId(String name, UUID parentFolderId);
}
