package com.example.gedappbackend.service;

import com.example.gedappbackend.model.AuditLog;
import com.example.gedappbackend.model.User;
import com.example.gedappbackend.repository.AuditLogRepository;
import com.example.gedappbackend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;
    private final HttpServletRequest request;

    public void log(UUID userId, String action, String resourceType, UUID resourceId, Map<String, Object> details) {
        try {
            User user = userId != null ? userRepository.findById(userId).orElse(null) : null;

            AuditLog auditLog = AuditLog.builder()
                    .user(user)
                    .action(action)
                    .resourceType(resourceType)
                    .resourceId(resourceId)
                    .details(details)
                    .ipAddress(getClientIp())
                    .userAgent(request.getHeader("User-Agent"))
                    .build();

            auditLogRepository.save(auditLog);

        } catch (Exception e) {
            log.error("Failed to save audit log", e);
        }
    }

    private String getClientIp() {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
