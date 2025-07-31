package com.frota_manager.inteligent_manager.model;

/**
 * Enum que representa os diferentes estados de um veículo na frota
 * Utilizado para controlar a disponibilidade e status operacional
 */
public enum VehicleStatus {
    AVAILABLE("Disponível"),
    IN_USE("Em uso"),
    MAINTENANCE("Em manutenção"),
    OUT_OF_SERVICE("Fora de serviço"),
    RESERVED("Reservado");
    
    private final String description;
    
    VehicleStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
} 