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

/**
 * DTO para criação de veículo
 */
public record CreateVehicleDto(
    String brand,
    String model,
    String licensePlate,
    Integer year,
    FuelType fuelType,
    Double fuelCapacity
) {}

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