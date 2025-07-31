package com.frota_manager.inteligent_manager.dto;

import java.time.LocalDate;

/**
 * DTO para criação de condutor
 */
public record CreateDriverDto(
    String name,
    String identificationNumber,
    String driverLicenseNumber,
    LocalDate licenseExpiryDate,
    String email,
    String phone
) {}