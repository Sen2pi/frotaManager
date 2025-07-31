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

/**
 * DTO para atualização de condutor
 */
public record UpdateDriverDto(
    String name,
    String email,
    String phone,
    DriverStatus status
) {}

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