package com.example.gedappbackend.mapper;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    @Mapping(target = "parentGroupId", source = "parentGroup.id")
    @Mapping(target = "subGroups", ignore = true)
    @Mapping(target = "users", ignore = true)
    GroupResponse toResponse(Group group);

    List<GroupResponse> toResponseList(List<Group> groups);
}
