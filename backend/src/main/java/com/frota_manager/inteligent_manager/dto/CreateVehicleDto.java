package com.frota_manager.inteligent_manager.dto;

import com.frota_manager.inteligent_manager.model.FuelType;

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