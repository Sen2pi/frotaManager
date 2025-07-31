package com.frota_manager.inteligent_manager.dto;

import com.frota_manager.inteligent_manager.model.VehicleStatus;

/**
 * DTO para resposta simplificada de veículo
 */
public record VehicleSummaryDto(
    Long id,
    String brand,
    String model,
    String licensePlate,
    VehicleStatus status,
    Double fuelPercentage,
    boolean needsMaintenance
) {}