package com.frota_manager.inteligent_manager.model;

/**
 * Enum que representa os diferentes estados de um condutor
 * Utilizado para controlar a disponibilidade e status operacional
 */
public enum DriverStatus {
    ACTIVE("Ativo"),
    INACTIVE("Inativo"),
    ON_TRIP("Em viagem"),
    ON_LEAVE("De férias"),
    SUSPENDED("Suspenso");
    
    private final String description;
    
    DriverStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
} 