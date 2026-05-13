package com.example.gedappbackend.mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "groupNames", ignore = true)
    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);

    @AfterMapping
    default void addGroupNames(@MappingTarget UserResponse response, User user) {
        if (user.getGroups() != null) {
            response.setGroupNames(
                    user.getGroups().stream()
                            .map(Group::getName)
                            .collect(Collectors.toList())
            );
        }
    }
}