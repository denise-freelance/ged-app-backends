package com.example.gedappbackend.mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface NotificationMapper {

    NotificationResponse toResponse(Notification notification);

    List<NotificationResponse> toResponseList(List<Notification> notifications);
}
