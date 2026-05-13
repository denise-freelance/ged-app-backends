package com.example.gedappbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "groups")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_group_id")
    private Group parentGroup;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany(mappedBy = "groups")
    private List<User> users;

    @OneToMany(mappedBy = "group")
    private List<Permission> permissions;
}
