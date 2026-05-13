package com.example.gedappbackend.mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "groupName", source = "group.name")
    PermissionResponse toResponse(Permission permission);

    List<PermissionResponse> toResponseList(List<Permission> permissions);
}
