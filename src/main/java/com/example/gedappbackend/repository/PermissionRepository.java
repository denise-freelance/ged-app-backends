package com.example.gedappbackend.repository;

import com.example.gedappbackend.model.Permission;
import com.example.gedappbackend.model.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    List<Permission> findByUserId(UUID userId);
    List<Permission> findByGroupId(UUID groupId);
    List<Permission> findByResourceIdAndResourceType(UUID resourceId, ResourceType resourceType);
    Optional<Permission> findByUserIdAndResourceIdAndResourceType(UUID userId, UUID resourceId, ResourceType resourceType);
}
