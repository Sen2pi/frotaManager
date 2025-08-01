package com.frota_manager.inteligent_manager.dto;

import com.frota_manager.inteligent_manager.model.NotificationPriority;
import com.frota_manager.inteligent_manager.model.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * DTO para criação de notificação
 */
public record NotificationCreateDto(
    @NotNull(message = "Tipo da notificação é obrigatório")
    NotificationType type,
    
    @NotNull(message = "Prioridade é obrigatória")
    NotificationPriority priority,
    
    @NotBlank(message = "Título é obrigatório")
    @Size(max = 200, message = "Título deve ter no máximo 200 caracteres")
    String title,
    
    @NotBlank(message = "Mensagem é obrigatória")
    @Size(max = 1000, message = "Mensagem deve ter no máximo 1000 caracteres")
    String message,
    
    @Size(max = 50, message = "Tipo da entidade deve ter no máximo 50 caracteres")
    String entityType,
    
    Long entityId,
    
    @Size(max = 500, message = "URL de ação deve ter no máximo 500 caracteres")
    String actionUrl,
    
    LocalDateTime expiresAt
) {}