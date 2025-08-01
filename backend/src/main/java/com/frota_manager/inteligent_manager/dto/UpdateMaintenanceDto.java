package com.frota_manager.inteligent_manager.dto;

import com.frota_manager.inteligent_manager.model.MaintenanceStatus;
import com.frota_manager.inteligent_manager.model.MaintenanceType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para atualização de manutenção
 */
public record UpdateMaintenanceDto(
    String description,
    MaintenanceType type,
    MaintenanceStatus status,
    LocalDateTime scheduledDate,
    LocalDateTime startDate,
    LocalDateTime completionDate,
    BigDecimal estimatedCost,
    BigDecimal actualCost,
    String technicianName,
    String workshopName,
    String notes
) {
    // Alias methods for compatibility
    public String technician() { return technicianName; }
    public String workshop() { return workshopName; }
}