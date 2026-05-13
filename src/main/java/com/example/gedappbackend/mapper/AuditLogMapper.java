package com.example.gedappbackend.mapper;

import com.example.gedappbackend.dto.response.AuditLogResponse;
import com.example.gedappbackend.model.AuditLog;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface AuditLogMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.username")
    AuditLogResponse toResponse(AuditLog auditLog);

    List<AuditLogResponse> toResponseList(List<AuditLog> auditLogs);
}
