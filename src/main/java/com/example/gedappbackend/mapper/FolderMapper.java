package com.example.gedappbackend.mapper;

@Mapper(componentModel = "spring")
public interface FolderMapper {

    @Mapping(target = "parentFolderId", source = "parentFolder.id")
    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "ownerName", source = "owner.username")
    @Mapping(target = "subFolders", ignore = true)
    @Mapping(target = "documents", ignore = true)
    FolderResponse toResponse(Folder folder);

    List<FolderResponse> toResponseList(List<Folder> folders);
}
