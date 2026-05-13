package com.example.gedappbackend.mapper;

@Mapper(componentModel = "spring")
public interface OfflineCacheMapper {

    @Mapping(target = "documentId", source = "document.id")
    @Mapping(target = "documentName", source = "document.name")
    OfflineCacheResponse toResponse(OfflineCache cache);

    List<OfflineCacheResponse> toResponseList(List<OfflineCache> caches);
}
