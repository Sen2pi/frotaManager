package com.frota_manager.inteligent_manager.dto;

import com.frota_manager.inteligent_manager.model.DriverStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTOs para Driver usando records Java para reduzir boilerplate code
 */
public record DriverDto(
    Long id,
    String name,
    String identificationNumber,
    String driverLicenseNumber,
    LocalDate licenseExpiryDate,
    String email,
    String phone,
    DriverStatus status,
    Integer totalTrips,
    Double totalKilometers,
    Double rating,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}

 