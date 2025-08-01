package com.frota_manager.inteligent_manager.dto;

import com.frota_manager.inteligent_manager.model.MaintenanceType;
import java.time.LocalDateTime;

/**
 * DTO para criação de manutenção
 */
public record CreateMaintenanceDto(
    Long vehicleId,
    String description,
    MaintenanceType type,
    LocalDateTime scheduledDate,
    String technicianName,
    String workshopName,
    java.math.BigDecimal estimatedCost,
    String notes
) {
    // Alias methods for compatibility
    public String technician() { return technicianName; }
    public String workshop() { return workshopName; }
}