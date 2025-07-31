package com.frota_manager.inteligent_manager.dto;

import com.frota_manager.inteligent_manager.model.FuelType;
import com.frota_manager.inteligent_manager.model.VehicleStatus;
import java.time.LocalDateTime;

/**
 * DTO para atualização de veículo
 */
public record UpdateVehicleDto(
    String brand,
    String model,
    VehicleStatus status,
    FuelType fuelType,
    Double fuelCapacity,
    Double currentFuelLevel,
    LocalDateTime nextMaintenanceDate
) {}