package com.example.gedappbackend.mapper;

import com.example.gedappbackend.dto.response.*;
import com.example.gedappbackend.model.*;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UserMapper.class, FolderMapper.class})
public interface DocumentMapper {

    @Mapping(target = "folderId", source = "folder.id")
    @Mapping(target = "folderPath", source = "folder.path")
    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "ownerName", source = "owner.username")
    @Mapping(target = "versions", ignore = true)
    DocumentResponse toResponse(Document document);

    List<DocumentResponse> toResponseList(List<Document> documents);

    @AfterMapping
    default void addVersions(@MappingTarget DocumentResponse response, Document document) {
        if (document.getVersions() != null) {
            response.setVersions(
                    document.getVersions().stream()
                            .map(v -> VersionResponse.builder()
                                    .id(v.getId())
                                    .versionNumber(v.getVersionNumber())
                                    .fileUrl(v.getFileUrl())
                                    .fileSize(v.getFileSize())
                                    .authorId(v.getAuthor() != null ? v.getAuthor().getId() : null)
                                    .authorName(v.getAuthor() != null ? v.getAuthor().getUsername() : null)
                                    .comment(v.getComment())
                                    .createdAt(v.getCreatedAt())
                                    .build())
                            .collect(Collectors.toList())
            );
        }
    }
}

