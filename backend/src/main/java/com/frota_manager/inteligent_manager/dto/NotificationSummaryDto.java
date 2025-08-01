package com.frota_manager.inteligent_manager.dto;

import com.frota_manager.inteligent_manager.model.NotificationPriority;
import com.frota_manager.inteligent_manager.model.NotificationType;

import java.time.LocalDateTime;

/**
 * DTO resumido para listagens de notificação
 */
public record NotificationSummaryDto(
    Long id,
    NotificationType type,
    NotificationPriority priority,
    String title,
    LocalDateTime createdAt,
    boolean isRead,
    boolean isExpired
) {}