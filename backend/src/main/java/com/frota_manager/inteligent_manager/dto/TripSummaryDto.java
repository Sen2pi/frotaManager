package com.frota_manager.inteligent_manager.dto;

import com.frota_manager.inteligent_manager.model.TripStatus;

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