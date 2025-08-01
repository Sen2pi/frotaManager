package com.frota_manager.inteligent_manager.dto;

import com.frota_manager.inteligent_manager.model.MaintenanceStatus;
import com.frota_manager.inteligent_manager.model.MaintenanceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTOs para Maintenance usando records Java para reduzir boilerplate code
 */
public record MaintenanceDto(
    Long id,
    Long vehicleId,
    String vehicleLicensePlate,
    String description,
    MaintenanceType type,
    MaintenanceStatus status,
    LocalDateTime scheduledDate,
    LocalDateTime startDate,
    LocalDateTime completionDate,
    BigDecimal cost,
    String technicianName,
    String workshopName,
    String notes,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}

 