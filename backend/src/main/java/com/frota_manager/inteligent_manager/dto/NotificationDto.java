package com.frota_manager.inteligent_manager.dto;

import com.frota_manager.inteligent_manager.model.NotificationPriority;
import com.frota_manager.inteligent_manager.model.NotificationType;

import java.time.LocalDateTime;

/**
 * DTO completo para notificação
 */
public record NotificationDto(
    Long id,
    NotificationType type,
    NotificationPriority priority,
    String title,
    String message,
    String entityType,
    Long entityId,
    LocalDateTime createdAt,
    LocalDateTime readAt,
    boolean isRead,
    String actionUrl,
    LocalDateTime expiresAt,
    boolean isExpired
) {}