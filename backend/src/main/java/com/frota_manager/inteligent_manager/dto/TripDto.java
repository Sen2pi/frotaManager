package com.frota_manager.inteligent_manager.dto;

import com.frota_manager.inteligent_manager.model.TripStatus;

import java.time.LocalDateTime;

/**
 * DTOs para Trip usando records Java para reduzir boilerplate code
 */
public record TripDto(
    Long id,
    Long vehicleId,
    String vehicleLicensePlate,
    Long driverId,
    String driverName,
    String origin,
    String destination,
    LocalDateTime startTime,
    LocalDateTime endTime,
    Double distanceKm,
    Double fuelConsumption,
    String purpose,
    TripStatus status,
    String notes,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}

 