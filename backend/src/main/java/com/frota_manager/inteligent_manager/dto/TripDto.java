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

/**
 * DTO para criação de viagem
 */
public record CreateTripDto(
    Long vehicleId,
    Long driverId,
    String origin,
    String destination,
    String purpose
) {}

/**
 * DTO para atualização de viagem
 */
public record UpdateTripDto(
    String origin,
    String destination,
    LocalDateTime startTime,
    LocalDateTime endTime,
    Double distanceKm,
    Double fuelConsumption,
    TripStatus status,
    String notes
) {}

/**
 * DTO para resposta simplificada de viagem
 */
public record TripSummaryDto(
    Long id,
    String vehicleLicensePlate,
    String driverName,
    String origin,
    String destination,
    TripStatus status,
    Double distanceKm,
    Long durationInMinutes
) {} 