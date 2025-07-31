package com.frota_manager.inteligent_manager.dto;

import com.frota_manager.inteligent_manager.model.MaintenanceStatus;
import com.frota_manager.inteligent_manager.model.MaintenanceType;
import java.time.LocalDateTime;

/**
 * DTO para resposta simplificada de manutenção
 */
public record MaintenanceSummaryDto(
    Long id,
    String vehicleLicensePlate,
    String description,
    MaintenanceType type,
    MaintenanceStatus status,
    LocalDateTime scheduledDate,
    boolean isOverdue
) {}