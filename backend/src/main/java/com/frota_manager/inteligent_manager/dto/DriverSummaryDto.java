package com.frota_manager.inteligent_manager.dto;

import com.frota_manager.inteligent_manager.model.DriverStatus;

/**
 * DTO para resposta simplificada de condutor
 */
public record DriverSummaryDto(
    Long id,
    String name,
    String driverLicenseNumber,
    DriverStatus status,
    boolean isLicenseValid,
    boolean isLicenseExpiringSoon,
    Double rating
) {}