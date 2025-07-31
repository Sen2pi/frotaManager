package com.frota_manager.inteligent_manager.dto;

import com.frota_manager.inteligent_manager.model.DriverStatus;

/**
 * DTO para atualização de condutor
 */
public record UpdateDriverDto(
    String name,
    String email,
    String phone,
    DriverStatus status
) {}