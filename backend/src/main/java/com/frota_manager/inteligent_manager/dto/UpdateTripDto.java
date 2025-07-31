package com.frota_manager.inteligent_manager.dto;

import com.frota_manager.inteligent_manager.model.TripStatus;
import java.time.LocalDateTime;

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