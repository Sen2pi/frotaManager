package com.frota_manager.inteligent_manager.dto;

import com.frota_manager.inteligent_manager.model.FuelType;
import com.frota_manager.inteligent_manager.model.VehicleStatus;

import java.time.LocalDateTime;

/**
 * DTOs para Vehicle usando records Java para reduzir boilerplate code
 * Records são imutáveis e geram automaticamente equals, hashCode e toString
 */
public record VehicleDto(
    Long id,
    String brand,
    String model,
    String licensePlate,
    Integer year,
    VehicleStatus status,
    FuelType fuelType,
    Double fuelCapacity,
    Double currentFuelLevel,
    Double totalKilometers,
    LocalDateTime lastMaintenanceDate,
    LocalDateTime nextMaintenanceDate,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}

 