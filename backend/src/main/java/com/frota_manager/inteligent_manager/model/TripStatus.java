package com.frota_manager.inteligent_manager.model;

/**
 * Enum que representa os diferentes estados de uma viagem
 * Utilizado para controlar o progresso e status das viagens
 */
public enum TripStatus {
    PLANNED("Planeada"),
    IN_PROGRESS("Em progresso"),
    COMPLETED("Concluída"),
    CANCELLED("Cancelada"),
    DELAYED("Atrasada");
    
    private final String description;
    
    TripStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
} 