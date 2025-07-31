package com.frota_manager.inteligent_manager.model;

/**
 * Enum que representa os diferentes estados de uma manutenção
 * Utilizado para controlar o progresso das manutenções
 */
public enum MaintenanceStatus {
    PLANNED("Planeada"),
    IN_PROGRESS("Em progresso"),
    COMPLETED("Concluída"),
    CANCELLED("Cancelada"),
    ON_HOLD("Em espera");
    
    private final String description;
    
    MaintenanceStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
} 