package com.example.gedappbackend.service;

import com.example.gedappbackend.model.*;
import com.example.gedappbackend.repository.DocumentRepository;
import com.example.gedappbackend.repository.FolderRepository;
import com.example.gedappbackend.repository.UserRepository;
import com.example.gedappbackend.repository.VersionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final VersionRepository versionRepository;
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;
    private final StorageServiceAWS storageServiceAWS;
    private final DocumentMapper documentMapper;
    private final NotificationService notificationService;
    private final AuditService auditService;

    @Transactional
    public DocumentResponse uploadDocument(DocumentUploadRequest request, MultipartFile file, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Folder folder = null;
        if (request.getFolderId() != null) {
            folder = folderRepository.findById(request.getFolderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Folder not found"));
        }

        // Upload file to S3
        String fileUrl = storageServiceAWS.uploadFile(file, userId);

        // Create document
        Document document = Document.builder()
                .name(request.getName() != null ? request.getName() : file.getOriginalFilename())
                .folder(folder)
                .owner(user)
                .type(getFileExtension(file.getOriginalFilename()))
                .size(file.getSize())
                .mimeType(file.getContentType())
                .currentVersion(1)
                .status(DocumentStatus.DRAFT)
                .tags(request.getTags())
                .build();

        document = documentRepository.save(document);

        // Create first version
        Version version = Version.builder()
                .document(document)
                .versionNumber(1)
                .fileUrl(fileUrl)
                .fileSize(file.getSize())
                .author(user)
                .comment("Initial upload")
                .build();
        versionRepository.save(version);

        // Audit
        auditService.log(userId, "UPLOAD", "DOCUMENT", document.getId(),
                Map.of("name", document.getName(), "size", file.getSize()));

        log.info("Document uploaded: {} by user {}", document.getName(), userId);

        return documentMapper.toResponse(document);
    }

    @Transactional(readOnly = true)
    public Page<DocumentResponse> getUserDocuments(UUID userId, Pageable pageable) {
        return documentRepository.findByOwnerId(userId, pageable)
                .map(documentMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public DocumentResponse getDocument(UUID documentId, UUID userId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        // Check permission
        if (!document.getOwner().getId().equals(userId) && !document.isPublic()) {
            throw new UnauthorizedException("You don't have permission to view this document");
        }

        return documentMapper.toResponse(document);
    }

    @Transactional
    public DocumentResponse updateDocument(UUID documentId, String newName, List<String> tags, UUID userId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        if (!document.getOwner().getId().equals(userId)) {
            throw new UnauthorizedException("You don't have permission to modify this document");
        }

        if (newName != null) {
            document.setName(newName);
        }
        if (tags != null) {
            document.setTags(tags);
        }

        document = documentRepository.save(document);

        auditService.log(userId, "UPDATE", "DOCUMENT", documentId,
                Map.of("name", document.getName()));

        return documentMapper.toResponse(document);
    }

    @Transactional
    public void deleteDocument(UUID documentId, UUID userId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        if (!document.getOwner().getId().equals(userId)) {
            throw new UnauthorizedException("You don't have permission to delete this document");
        }

        // Delete from S3
        List<Version> versions = versionRepository.findByDocumentIdOrderByVersionNumberDesc(documentId);
        for (Version version : versions) {
            storageServiceAWS.deleteFile(version.getFileUrl());
        }

        documentRepository.delete(document);

        auditService.log(userId, "DELETE", "DOCUMENT", documentId,
                Map.of("name", document.getName()));

        log.info("Document deleted: {} by user {}", document.getName(), userId);
    }

    @Transactional
    public DocumentResponse addVersion(UUID documentId, MultipartFile file, String comment, UUID userId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        if (!document.getOwner().getId().equals(userId)) {
            throw new UnauthorizedException("You don't have permission to modify this document");
        }

        User user = userRepository.findById(userId).get();

        // Upload new version
        String fileUrl = storageServiceAWS.uploadFile(file, userId);

        int newVersionNumber = document.getCurrentVersion() + 1;

        Version version = Version.builder()
                .document(document)
                .versionNumber(newVersionNumber)
                .fileUrl(fileUrl)
                .fileSize(file.getSize())
                .author(user)
                .comment(comment)
                .build();
        versionRepository.save(version);

        // Update document
        document.setCurrentVersion(newVersionNumber);
        document.setSize(file.getSize());
        document.setUpdatedAt(LocalDateTime.now());
        document = documentRepository.save(document);

        auditService.log(userId, "NEW_VERSION", "DOCUMENT", documentId,
                Map.of("version", newVersionNumber));

        return documentMapper.toResponse(document);
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "unknown";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }
}
