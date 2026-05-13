package com.example.gedappbackend.model;

public enum UserRole {
    ADMIN,      // Administrateur complet
    MANAGER,    // Gestionnaire (peut valider, gérer des groupes)
    EDITOR,     // Éditeur (peut modifier documents)
    READER,     // Lecteur seul
    GUEST       // Invité (accès limité)
}