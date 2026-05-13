package com.example.gedappbackend.repository;

import com.example.gedappbackend.model.Connector;
import com.example.gedappbackend.model.ConnectorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConnectorRepository extends JpaRepository<Connector, UUID> {
    List<Connector> findByUserId(UUID userId);
    Optional<Connector> findByUserIdAndType(UUID userId, ConnectorType type);
    List<Connector> findByIsConnectedTrue();
}
