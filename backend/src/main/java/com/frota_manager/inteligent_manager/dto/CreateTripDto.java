package com.frota_manager.inteligent_manager.dto;

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