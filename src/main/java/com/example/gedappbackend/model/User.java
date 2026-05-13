package com.example.gedappbackend.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Column(unique = true, nullable = false)
        private String username;

        @Column(unique = true, nullable = false)
        private String email;

        @Column(nullable = false)
        private String passwordHash;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private UserRole role;

        private String avatarUrl;

        @Builder.Default
        private boolean isActive = true;

        private LocalDateTime lastLogin;

        @Builder.Default
        private LocalDateTime createdAt = LocalDateTime.now();

        private LocalDateTime updatedAt;

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(
                name = "user_groups",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "group_id")
        )
        private List<Group> groups;

        @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
        private List<Document> documents;

        @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
        private List<Comment> comments;

        @PreUpdate
        protected void onUpdate() {
            updatedAt = LocalDateTime.now();
        }

        // UserDetails methods
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
        }

        @Override
        public String getPassword() {
            return passwordHash;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return isActive;
        }
}