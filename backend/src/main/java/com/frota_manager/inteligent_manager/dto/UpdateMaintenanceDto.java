package com.frota_manager.inteligent_manager.dto;

import com.frota_manager.inteligent_manager.model.MaintenanceStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para atualização de manutenção
 */
public record UpdateMaintenanceDto(
    String description,
    MaintenanceStatus status,
    LocalDateTime startDate,
    LocalDateTime completionDate,
    BigDecimal cost,
    String technicianName,
    String workshopName,
    String notes
) {}